package ing.bank.app.controllers;


import ing.bank.app.models.User;
import ing.bank.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/Users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String getAlive(){
        return "Its alive!";
    }

    @GetMapping(path="/test")
    public String test() {
        User user = new User();
        user.setName("testUser");
        user.setEmail("testUser@email");
        userRepository.save(user);
        return "new user created";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAll() {
        return userRepository.findAll();
    }
}
