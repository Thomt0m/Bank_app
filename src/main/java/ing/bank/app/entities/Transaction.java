package ing.bank.app.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // TODO look into if executor should only be some identifying value (like a name), as the executing party can be external, meaning it is not present in the database
    private String creator;

    private UUID sendingAccountId;

    private UUID receivingAccountId;

    private BigDecimal amount;

    private String description;

    private LocalDateTime creationDate;

    private LocalDateTime executionDate;




    public UUID getId() { return id; }
    public Transaction setId(UUID id) { this.id = id; return this; }

    public String getCreator() { return creator; }
    public Transaction setCreator(String creator) { this.creator = creator; return this; }

    public UUID getSendingAccountId() { return sendingAccountId; }
    public Transaction setSendingAccountId(UUID sendingAccountId) { this.sendingAccountId = sendingAccountId; return this; }

    public UUID getReceivingAccountId() { return receivingAccountId; }
    public Transaction setReceivingAccountId(UUID receivingAccountId) { this.receivingAccountId = receivingAccountId; return this; }

    public BigDecimal getAmount() { return amount; }
    public Transaction setAmount(BigDecimal amount) { this.amount = amount.abs(); return this; }

    public String getDescription() { return description; }
    public Transaction setDescription(String description) { this.description = description; return this; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public Transaction setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; return this; }

    public LocalDateTime getExecutionDate() { return executionDate; }
    public Transaction setExecutionDate(LocalDateTime executionDate) { this.executionDate = executionDate; return this; }
}
