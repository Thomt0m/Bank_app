package ing.bank.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

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


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Iterable<Account> getAccounts() { return accounts; }
    public boolean addAccount(Account account) {
        account.setOwner(this);
        return accounts.add(account);
    }


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
