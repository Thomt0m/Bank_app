package ing.bank.app.controller;


import ing.bank.app.entities.Account;
import ing.bank.app.entities.User;
import ing.bank.app.repositories.AccountRepository;
import ing.bank.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping(path="/Users")
public class UserController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping
    public @ResponseBody String getAlive(){
        return "Its alive!";
    }

    @PostMapping(path="/Register")
    public @ResponseBody String registerNewUser(@RequestBody String name) {
        User user = new User(
                "testUser",
                "testUser@email.com",
                passwordEncoder.encode("password")
        );
        userRepo.save(user);
        return "new user created";
    }

    @GetMapping(path="/testAccount")
    public @ResponseBody String testAccount() {
        User user = new User(
                "testUser",
                "testUser@email.com",
                passwordEncoder.encode("password")
        );
        userRepo.save(user);

        Account account = new Account();
        account.setOwner(user);
        account.addBalance(BigDecimal.valueOf(10));
        accountRepository.save(account);

        return "new account created";
    }



}
