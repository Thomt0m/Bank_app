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

        transactionRepo.save(transaction);

        Optional<Account> senderOpt = accountRepo.findById(transaction.getSendingAccountId());
        Optional<Account> receiverOpt = accountRepo.findById(transaction.getReceivingAccountId());

        // If both parties are not in the DB, the transaction changes/affects nothing and there's nothing to execute.
        // However, the creator might have expected it would have an effect, but have provided the wrong account Ids,
        // so something is logged, at least for now.
        if (senderOpt.isEmpty() && receiverOpt.isEmpty()) {
            System.out.printf(
                    "Unable to find any parties of the transaction in the database, senderId: %s, receiverId: %s.",
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

        // TODO figure out if repo.saveAll() saves all or none of the entities, or if it can save only some
        //  As the transaction needs to happen in full, or not at all.
        //Iterable<Account> result = accountRepo.saveAll(accounts);

        /*int counter = 0;
        for (Account ignored: result)
            counter++;
        if (counter != accounts.size())
            return false;*/

        transaction.setExecutionDate(LocalDateTime.now());
        transactionRepo.save(transaction);
        return true;
    }

}
