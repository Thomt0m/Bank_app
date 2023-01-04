package ing.bank.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ing.bank.app.models.NewUser;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @OneToMany(mappedBy = "owner",cascade = CascadeType.ALL)
    @JsonIgnore
    private final Set<Account> accounts = new HashSet<>();

    private String passwordHash;

    private String passwordHash2;




    public Long getId() { return id; }
    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() { return name; }
    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() { return email; }
    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public Iterable<Account> getAccounts() { return accounts; }
    public Optional<Account> getAccount(UUID accountId) { return accounts.stream().filter(x -> x.getId().equals(accountId)).findFirst(); }
    public boolean addAccount(Account account) {
        account.setOwner(this);
        return accounts.add(account);
    }

    public String getPasswordHash() { return passwordHash; }
    public User setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        return this;
    }

    public String getPasswordHash2() { return passwordHash2; }
    public User setPasswordHash2(String passwordHash) {
        this.passwordHash2 = passwordHash;
        return this;
    }




    public User() {}

    public User(String name, String email, String passwordHash){
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    /*public User(NewUser newUser) {
        this.name = newUser.Name;
        this.email = newUser.Email;
        this.passwordHash = newUser.Password.GetHash();
    }*/




    @Override
    public String toString() {
        return String.format(
                "UserId:%d, Name:%s, Email:%s",
                id, name, email
        );
    }

    public Boolean equals(User other) {
        // TODO figure out why I am allowed to access other.id, seeing as it's a private field of User
        return
                this == other ^
                this.id.equals(other.id);
    }
}
