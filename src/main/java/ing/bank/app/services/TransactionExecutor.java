package ing.bank.app.services;

import ing.bank.app.entities.Account;
import ing.bank.app.entities.Transaction;
import ing.bank.app.repositories.AccountRepository;
import ing.bank.app.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class TransactionExecutor {

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private AccountRepository accountRepo;



    // TODO ensure 'executeTransaction' executes as a single statement, ie a proper transaction.
    //  Meaning that either all steps are executed, or none of them are.
    //  This can probably best be done by implementing a single custom SQL statement, that updates all records involved
    public boolean executeTransaction(Transaction transaction) {

        // TODO implement some checks on the transaction before execution,
        //  like ensuring the transaction is actually saved in the database,
        //  and that its fields are set correctly.

        Optional<Account> senderOpt = accountRepo.findById(transaction.getSendingAccountId());
        Optional<Account> receiverOpt = accountRepo.findById(transaction.getReceivingAccountId());

        // If both parties are not in the DB, the transaction changes/affects nothing and would be useless
        // But the creator might have expected it to have an effect, only provided the wrong account Ids
        if (senderOpt.isEmpty() && receiverOpt.isEmpty()){
            System.out.printf(
                    "Both parties of the transaction were not found in the database, senderId: %s, receiverId: %s.",
                    transaction.getSendingAccountId(),
                    transaction.getReceivingAccountId());
            return false;
        }

        ArrayList<Account> accounts = new ArrayList<>();

        if (senderOpt.isPresent()) {
            Account sender = senderOpt.get();
            sender.addBalance(transaction.getAmount().negate());
            accountRepo.save(sender);
            accounts.add(sender);
        }

        if (receiverOpt.isPresent()){
            Account receiver = receiverOpt.get();
            receiver.addBalance(transaction.getAmount());
            accountRepo.save(receiver);
            accounts.add(receiver);
        }

        // TODO figure out is repo.saveAll() saves all or none of the entities, or if it can save some
        //  The transaction needs to happen fully or not at all.
        /*Iterable<Account> result = accountRepo.saveAll(accounts);
        int counter = 0;
        for (Account ignored: result)
            counter++;
        if (counter != accounts.size())
            return false;*/

        transaction.setExecutionDate(LocalDateTime.now());
        transactionRepo.save(transaction);
        return true;
    }

}
