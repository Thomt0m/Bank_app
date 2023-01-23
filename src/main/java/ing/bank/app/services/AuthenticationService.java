package ing.bank.app.services;

import ing.bank.app.entities.User;
import ing.bank.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;




@Component
public class AuthenticationService implements AuthenticationProvider {

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;


    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        System.out.printf("AuthService.authenticate(%s)%n", auth);
        String username = auth.getName();
        String[] credentials = StringUtils.split(auth.getCredentials().toString(), String.valueOf(Character.LINE_SEPARATOR));
        if (credentials == null || credentials.length != 2){
            System.out.printf("AuthService: credentials not valid - %s%n", auth.getCredentials().toString());
            throw new BadCredentialsException("invalid login");
        }

        String password = credentials[0];
        String password2 = credentials[1];

        User user = userRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("invalid login"));

        if (user.isEnabled() && passwordService.matches(password, user.getPassword()) && user.getPassword2().equals(password2)) {
            System.out.println("AuthService: user successfully authenticated");
            return auth;
        }
        else {
            System.out.println("AuthService: invalid password(s)");
            throw new BadCredentialsException("invalid login");
        }
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }

}
