package ing.bank.app.controller;


import ing.bank.app.entities.User;
import ing.bank.app.models.NewUser;
import ing.bank.app.repositories.AccountRepository;
import ing.bank.app.repositories.UserRepository;
import ing.bank.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(path="/Users")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountRepository accountRepository;


    @PostMapping(path="/Register")
    public @ResponseBody String registerNewUser(@RequestBody NewUser newUser) {
        User user = userService.registerNewUser(newUser);
        return String.format("new user '%s' created", user.getUsername());
    }

}
