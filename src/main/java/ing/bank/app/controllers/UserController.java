package ing.bank.app.controllers;


import ing.bank.app.entities.Account;
import ing.bank.app.entities.User;
import ing.bank.app.repositories.AccountRepository;
import ing.bank.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

@Controller
@RequestMapping(path="/Users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping
    public @ResponseBody String getAlive(){
        return "Its alive!";
    }

    @GetMapping(path="/test")
    public @ResponseBody String test() {
        User user = new User();
        user.setName("testUser");
        user.setEmail("testUser@email");
        userRepository.save(user);
        return "new user created";
    }

    @GetMapping(path="/testAccount")
    public @ResponseBody String testAccount() {
        User user = new User();
        user.setName("testUser");
        user.setEmail("testUser@email");
        userRepository.save(user);

        Account account = new Account();
        account.setOwner(user);
        account.addBalance(BigDecimal.valueOf(10));
        accountRepository.save(account);

        return "new account created";
    }

    @GetMapping(path="/AllUsers")
    public @ResponseBody Iterable<User> getAll() { return userRepository.findAll(); }

    @GetMapping(path="/AllAccounts")
    public @ResponseBody Iterable<Account> getAllAccounts() { return accountRepository.findAll(); }

}
