package ing.bank.app;

import ing.bank.app.entities.User;
import ing.bank.app.services.PasswordService;
import ing.bank.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {


    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    public void initialize(final AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .withUser(User.builder()
                        .email("testUser@email.com")
                        .password("password")
                        .passwordEncoder(str -> passwordService.getPasswordEncoder().encode(str))
                        .roles("USER", "TEST")
                        .build())
                .and()
                .userDetailsService(userService)
                .passwordEncoder(passwordService.getPasswordEncoder())
        ;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/", "/home", "/Test/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login").permitAll())
                .logout(logout -> logout.permitAll());

        return http.build();
    }
}
