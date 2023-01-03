package ing.bank.app.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
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
    public void setId(UUID id) { this.id = id; }

    public String getCreator() { return creator; }
    public void setCreator(String creator) { this.creator = creator; }

    public UUID getSendingAccountId() { return sendingAccountId; }
    public void setSendingAccountId(UUID sendingAccountId) { this.sendingAccountId = sendingAccountId; }

    public UUID getReceivingAccountId() { return receivingAccountId; }
    public void setReceivingAccountId(UUID receivingAccountId) { this.receivingAccountId = receivingAccountId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount.abs(); }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public LocalDateTime getExecutionDate() { return executionDate; }
    public void setExecutionDate(LocalDateTime executionDate) { this.executionDate = executionDate; }
}
