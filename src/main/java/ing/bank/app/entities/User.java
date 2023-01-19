package ing.bank.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

/**
 * Largely modeled after, copied from, the {@link org.springframework.security.core.userdetails.User} class from spring-security-core-6.0.1, as written by Ben Alex & Luke Taylor
 * <p>
 * Models core user information retrieved by a {@link UserDetailsService}.
 */
@Entity
@Table(name = "users")
public class User implements UserDetails, CredentialsContainer {

    @Id
    @Column(name = "username")
    private String email;

    private String fullName;

    private String password;

    private String password2;

    private Boolean enabled = true;

    @ElementCollection
    @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "username"))
    @Column(name = "authority")
    private Set<GrantedAuthority> authorities = new HashSet<>();

    @OneToMany(mappedBy = "owner",cascade = CascadeType.ALL)
    @JsonIgnore
    private final Set<Account> accounts = new HashSet<>();

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return this.password; }
    public void setPassword(String passwordHashed) { this.password = passwordHashed; }

    public String getPassword2() { return password2; }
    public void setPassword2(String password2Hashed) { this.password2 = password2Hashed; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    public Iterable<Account> getAccounts() { return accounts; }
    public Optional<Account> getAccount(UUID accountId) { return accounts.stream().filter(x -> x.getId().equals(accountId)).findFirst(); }
    public boolean addAccount(Account account) {
        account.setOwner(this);
        return accounts.add(account);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
    public Boolean addGrantedAuthority(GrantedAuthority grantedAuthority) {
        if (grantedAuthority == null) return false;
        return authorities.add(grantedAuthority);
    }
    public Boolean removeGrantedAuthority(GrantedAuthority grantedAuthority) {
        return authorities.remove(grantedAuthority);
    }

    @Override
    public String getUsername() { return email; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return getEnabled(); }



    public User() {
        // Entities are required to have a public or protected no-argument constructor
        this.fullName = "Empty User";
        setEmail("EmptyUser@email.com");
    }

    public User(String fullName, String email, String passwordHashed) {
        this.fullName = fullName;
        this.email = email;
        this.password = passwordHashed;
    }

    /**
     * Construct the <code>User</code> with the details required by
     * {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider}.
     * @param username the username presented to the
     * <code>DaoAuthenticationProvider</code>
     * @param password the password that should be presented to the
     * <code>DaoAuthenticationProvider</code>
     * @param enabled set to <code>true</code> if the user is enabled
     * @param authorities the authorities that should be granted to the caller if they
     * presented the correct username and password and the user is enabled. Not null.
     * @throws IllegalArgumentException if a <code>null</code> value was passed either as
     * a parameter or as an element in the <code>GrantedAuthority</code> collection
     */
    public User(String username, String password, boolean enabled, Collection<? extends GrantedAuthority> authorities) {
        Assert.isTrue(username != null && !"".equals(username) && password != null,
                "Cannot pass null or empty values to constructor");
        this.email = username;
        this.password = password;
        this.enabled = enabled;
        this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
    }




    @Override
    public String toString() {
        return String.format(
                "Email:%s, Name:%s",
                email, fullName
        );
    }

    public Boolean equals(User other) {
        // TODO figure out why I am allowed to access fields like other.email, seeing as it's a private field of User
        return
                this == other ^
                this.getUsername().equals(other.getUsername());
    }

    @Override
    public boolean equals(Object obj) {
        return
                this == obj ^
                obj instanceof User &&
                this.getUsername().equals(((User)obj).getUsername());
    }

    @Override
    public int hashCode() {
        return this.getUsername().hashCode();
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        // Ensure array iteration order is predictable (as per
        // UserDetails.getAuthorities() contract and SEC-717)
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(new AuthorityComparator());
        for (GrantedAuthority grantedAuthority : authorities) {
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }
        return sortedAuthorities;
    }

    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {

        private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

        @Override
        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            // Neither should ever be null as each entry is checked before adding it to
            // the set. If the authority is null, it is a custom authority and should
            // precede others.
            if (g2.getAuthority() == null) {
                return -1;
            }
            if (g1.getAuthority() == null) {
                return 1;
            }
            return g1.getAuthority().compareTo(g2.getAuthority());
        }

    }

    /**
     * Creates a new UserBuilder
     * @return the UserBuilder
     */
    public static UserBuilder builder() {
        return new UserBuilder();
    }


    /**
     * Builds the {@link User} to be added. At minimum the email, password, passwordEncoder and authorities
     * should be provided. The remaining attributes have reasonable defaults.
     */
    public static final class UserBuilder {

        private String email;

        private String password;

        private List<GrantedAuthority> authorities;

        private boolean enabled = true;

        private Function<String, String> passwordEncoder;


        private UserBuilder() {}


        /**
         * Populates the email. This attribute is required.
         * @param email the email. Cannot be null.
         * @return the {@link UserBuilder} for method chaining (i.e. to populate
         * additional attributes for this User)
         */
        public UserBuilder email(String email) {
            Assert.notNull(email, "email cannot be null");
            this.email = email;
            return this;
        }

        /**
         * Populates the password. This attribute is required.
         * @param password the raw password. Cannot be null.
         * @return the {@link UserBuilder} for method chaining (i.e. to populate
         * additional attributes for this User)
         */
        public UserBuilder password(String password) {
            Assert.notNull(password, "password cannot be null");
            this.password = password;
            return this;
        }

        /**
         * Encodes the current password (if non-null) and any future passwords supplied to
         * {@link #password(String)}.
         * @param encoder the encoder to use
         * @return the {@link UserBuilder} for method chaining (i.e. to populate
         * additional attributes for this User)
         */
        public UserBuilder passwordEncoder(Function<String, String> encoder) {
            Assert.notNull(encoder, "encoder cannot be null");
            this.passwordEncoder = encoder;
            return this;
        }

        /**
         * Populates the roles. This method is a shortcut for calling
         * {@link #authorities(String...)}, but automatically prefixes each entry with
         * "ROLE_". This means the following:
         *
         * <code>
         *     builder.roles("USER","ADMIN");
         * </code>
         *
         * is equivalent to
         *
         * <code>
         *     builder.authorities("ROLE_USER","ROLE_ADMIN");
         * </code>
         *
         * <p>
         * This attribute is required, but can also be populated with
         * {@link #authorities(String...)}.
         * </p>
         * @param roles the roles for this user (i.e. USER, ADMIN, etc). Cannot be null,
         * contain null values or start with "ROLE_"
         * @return the {@link UserBuilder} for method chaining (i.e. to populate
         * additional attributes for this User)
         */
        public UserBuilder roles(String... roles) {
            List<GrantedAuthority> authorities = new ArrayList<>(roles.length);
            for (String role : roles) {
                authorities.add(new GrantedAuthority(role));
            }
            return authorities(authorities);
        }

        /**
         * Populates the authorities. This attribute is required.
         * @param authorities the authorities for this user. Cannot be null, or contain
         * null values
         * @return the {@link UserBuilder} for method chaining (i.e. to populate
         * additional attributes for this User)
         * @see #roles(String...)
         */
        public UserBuilder authorities(GrantedAuthority... authorities) {
            return authorities(Arrays.asList(authorities));
        }

        /**
         * Populates the authorities. This attribute is required.
         * @param authorities the authorities for this user. Cannot be null, or contain
         * null values
         * @return the {@link UserBuilder} for method chaining (i.e. to populate
         * additional attributes for this User)
         * @see #roles(String...)
         */
        public UserBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
            this.authorities = new ArrayList<>(authorities);
            return this;
        }

        /**
         * Populates the authorities. This attribute is required.
         * @param authorities the authorities for this user (i.e. ROLE_USER, ROLE_ADMIN,
         * etc). Cannot be null, or contain null values
         * @return the {@link UserBuilder} for method chaining (i.e. to populate
         * additional attributes for this User)
         * @see #roles(String...)
         */
        public UserBuilder authorities(String... authorities) {
            return roles(authorities);
        }

        /**
         * Defines if the account is enabled or not. Default is true.
         * @param enabled true if the account is enabled, false otherwise
         * @return the {@link UserBuilder} for method chaining (i.e. to populate
         * additional attributes for this User)
         */
        public UserBuilder enabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public UserDetails build() {
            String encodedPassword = passwordEncoder.apply(password);
            return new User(email, encodedPassword, enabled, authorities);
        }

    }
}
