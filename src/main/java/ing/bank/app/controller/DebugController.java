package ing.bank.app.controller;


import ing.bank.app.entities.Account;
import ing.bank.app.entities.Transaction;
import ing.bank.app.entities.User;
import ing.bank.app.repositories.AccountRepository;
import ing.bank.app.repositories.TransactionRepository;
import ing.bank.app.repositories.UserRepository;
import ing.bank.app.services.TransactionExecutor;
import org.springframework.beans.factory.annotation.Autowired;
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




    @GetMapping("/DoUserTransaction")
    public @ResponseBody Iterable<String> doUserTransaction() {
        ArrayList<String> response = new ArrayList<>();

        // Create users
        User user1 = new User()
                .setName("testUser1")
                .setEmail("testUser1@email.com");
        Account account1 = new Account();
        account1.addBalance(new BigDecimal(1000));
        user1.addAccount(account1);
        userRepo.save(user1);

        User user2 = new User()
                .setName("testUser2")
                .setEmail("testUser2@email.com");
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





    @GetMapping("/createEntities")
    public @ResponseBody String createEntities(){
        if (!userRepo.existsById(1L)){
            User user1 = new User();
            user1.setName("testUser1");
            user1.setEmail("testUser1@email.com");

            Account account1 = new Account();
            account1.addBalance(new BigDecimal(1234));
            System.out.printf("account 1 balance = %f%n", account1.getBalance().doubleValue());
            user1.addAccount(account1);

            userRepo.save(user1);
        }

        if (!userRepo.existsById(2L)){
            User user2 = new User();
            user2.setName("testUser2");
            user2.setEmail("testUser2@email.com");

            Account account2 = new Account();
            user2.addAccount(account2);

            userRepo.save(user2);
        }

        return "Test users and account created";
    }



    @GetMapping("/tryTransaction")
    public @ResponseBody String tryTransaction(){

        if(!userRepo.existsById(1L) || !userRepo.existsById(2L))
            return "Unable to find Users";

        User user1 = userRepo.findById(1L).orElseThrow();
        Account account1 = user1.getAccounts().iterator().next();
        User user2 = userRepo.findById(2L).orElseThrow();
        Account account2 = user2.getAccounts().iterator().next();

        String initialState = String.format("Start -- account 1 = %f, account 2 = %f\n", account1.getBalance(), account2.getBalance());

        Transaction transaction = new Transaction();
        transaction.setCreationDate(LocalDateTime.now());
        transaction.setCreator("Debugging");
        transaction.setDescription("Testing transaction execution");
        transaction.setSendingAccountId(account1.getId());
        transaction.setReceivingAccountId(account2.getId());
        transaction.setAmount(new BigDecimal(100));
        transactionRepo.save(transaction);

        transactionExecutor.executeTransaction(transaction);

        return String.format(initialState + "End -- account 1 = %f, account 2 = %f", account1.getBalance(), account2.getBalance());
    }



}
