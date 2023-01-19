package ing.bank.app.entities;

import org.springframework.util.Assert;

/**
 * Basic concrete implementation of a {@link org.springframework.security.core.GrantedAuthority}.
 *
 * <p>
 * Stores a {@code String} representation of an authority granted to the
 * {@link org.springframework.security.core.Authentication Authentication} object.
 *
 * Based on {@link org.springframework.security.core.authority.SimpleGrantedAuthority}, as written by Luke Taylor
 */
public class GrantedAuthority implements org.springframework.security.core.GrantedAuthority {

    private String role;

    public GrantedAuthority(String role) {
        Assert.hasText(role, "A textual representation of the granted authority is required");
        this.role = role.toUpperCase();
    }

    @Override
    public String getAuthority() {
        return this.role;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof GrantedAuthority)
            return this.role.equals(((GrantedAuthority) obj).role);
        return false;
    }

    @Override
    public int hashCode() {
        return this.role.hashCode();
    }

    @Override
    public String toString() {
        return this.role;
    }
}
