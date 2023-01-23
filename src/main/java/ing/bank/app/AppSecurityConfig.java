package ing.bank.app;

import ing.bank.app.entities.User;
import ing.bank.app.filters.AuthenticationFilter;
import ing.bank.app.repositories.UserRepository;
import ing.bank.app.services.AuthenticationService;
import ing.bank.app.services.PasswordService;
import ing.bank.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
    private UserRepository userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private AuthenticationService authService;



    @Autowired
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .and()
                .userDetailsService(userService)
                .passwordEncoder(passwordService.getPasswordEncoder())
                .and()
                .authenticationProvider(authService)
        ;

        userService.register(
                User.builder()
                        .email("testUser@email.com")
                        .fullName("Test User")
                        .password("password")
                        .password2("password2")
                        .passwordEncoder(str -> passwordService.encode(str))
                        .roles("USER", "TEST")
                        .build()
        );
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilter(new AuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)))//, UsernamePasswordAuthenticationFilter.class
                )
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/", "/home", "/Test/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login").permitAll())
                .logout(logout -> logout.permitAll());

        return http.build();
    }
}
