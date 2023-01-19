package ing.bank.app.services;

import ing.bank.app.entities.User;
import ing.bank.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO figure out if there is a way to have spring generate a matching statement for a query
        for (User user : userRepo.findAll())
            if (user.getUsername().equals(username))
                return user;
        throw new UsernameNotFoundException("Username = " + username);
    }
}
