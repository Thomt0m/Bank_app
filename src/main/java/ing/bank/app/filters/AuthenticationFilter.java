package ing.bank.app.filters;


import ing.bank.app.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.Assert;


import java.util.Collections;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {


    public AuthenticationFilter(AuthenticationManager authManager) {
        Assert.notNull(authManager, "AuthenticationManager cannot be null, is required");
        this.setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("AuthenticationFilter.attemptAuthentication()");
        UsernamePasswordAuthenticationToken authToken = getAuthToken(request);
        setDetails(request, authToken);
        return getAuthenticationManager().authenticate(authToken);
    }

    private UsernamePasswordAuthenticationToken getAuthToken(final HttpServletRequest request) {
        String username = obtainUsername(request);
        String password = obtainPassword(request);
        String password2 = obtainPassword2(request);

        // TODO figure out if it is 'good-practice' to put (expose) the password in the token?
        //  UsernamePasswordAuthenticationFilter does do this as well.
        //  Why not give out some sort of session-key instead?
        return new UsernamePasswordAuthenticationToken(
                username,
                password +
                        Character.LINE_SEPARATOR +
                        password2,
                Collections.emptyList()
                );
    }

    protected String obtainPassword2(HttpServletRequest request) {
        return request.getParameter("password2");
    }
}
