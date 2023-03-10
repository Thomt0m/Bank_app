package ing.bank.app.services;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Bean
    public PasswordEncoder getPasswordEncoder() { return passwordEncoder; }

    public String encode(CharSequence password) {
        return passwordEncoder.encode(password);
    }

    public boolean matches(CharSequence password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
    }
}
