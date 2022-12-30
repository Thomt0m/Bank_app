package ing.bank.app.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {

    // TODO see if you can use an IBAN as id for Account
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(name = "ownerId")
    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private User owner;

    private BigDecimal balance = BigDecimal.valueOf(0, 10);



    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public BigDecimal addBalance(BigDecimal value) { return this.balance.add(value); }

}
