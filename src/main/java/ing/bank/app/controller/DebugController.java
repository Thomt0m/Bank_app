package ing.bank.app.controller;


import ing.bank.app.entities.Account;
import ing.bank.app.entities.User;
import ing.bank.app.entities.Transaction;
import ing.bank.app.repositories.AccountRepository;
import ing.bank.app.repositories.TransactionRepository;
import ing.bank.app.repositories.UserRepository;
import ing.bank.app.services.TransactionExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * A controller for debugging and testing purposes, not meant for 'production'
 */
@Controller
@RequestMapping(path="/Test")
public class DebugController {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepo;
    @Autowired
    AccountRepository accountRepo;
    @Autowired
    TransactionRepository transactionRepo;
    @Autowired
    TransactionExecutor transactionExecutor;


    @GetMapping(path="/GetAllUsers")
    public @ResponseBody Iterable<User> getAll() { return userRepo.findAll(); }

    @GetMapping(path="/GetAllAccounts")
    public @ResponseBody Iterable<Account> getAllAccounts() { return accountRepo.findAll(); }

    @GetMapping(path = "/GetAllTransactions")
    public @ResponseBody Iterable<Transaction> getAllTransactions() { return transactionRepo.findAll(); }


    /**
     * Demo showing how to execute a transaction, starting with nothing and creating everything that's required.
     * To verify the persistence of the {@link User}(s), {@link Account}(s) and the {@link Transaction}, check the database.
     * @return A collection of strings showing the states of the accounts before and after the transaction
     */
    @GetMapping("/DoUserTransaction")
    public @ResponseBody Iterable<String> doUserTransaction() {
        ArrayList<String> response = new ArrayList<>();

        // Create users
        User user1 = new User(
                "testUser1",
                "testUser1@email.com",
                passwordEncoder.encode("password1")
        );
        Account account1 = new Account();
        account1.addBalance(new BigDecimal(1000));
        user1.addAccount(account1);
        userRepo.save(user1);

        User user2 = new User(
                "testUser2",
                "testUser2@email.com",
                passwordEncoder.encode("password2")
        );
        Account account2 = new Account();
        user2.addAccount(account2);
        userRepo.save(user2);

        // Log the initial state of the accounts
        response.add(String.format("INITIAL -- account1 = %f, account2 = %f", account1.getBalance(), account2.getBalance()));

        // Create the transaction
        Transaction transaction = new Transaction()
                .setCreationDate(LocalDateTime.now())
                .setCreator("Debugging")
                .setDescription("Testing transaction execution")
                .setSendingAccountId(account1.getId())
                .setReceivingAccountId(account2.getId())
                .setAmount(new BigDecimal(100));

        // Do the transaction
        transactionExecutor.executeTransaction(transaction);

        // Log the resulting state of the accounts
        response.add(String.format("RESULT  -- account1 = %f, account2 = %f", account1.getBalance(), account2.getBalance()));

        return response;
    }

}
