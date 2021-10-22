package sec.eci.poc.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sec.eci.poc.jwt.JwtProvider;


@Service
public class POCUserService {

    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JwtProvider jwtProvider;
    @Autowired
    POCUserRepo userRepo;

    @Autowired
    public POCUserService(AuthenticationManager authenticationManager,
                       PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    /**
     * Sign in a user into the application, with JWT-enabled authentication
     *
     * @param username  username
     * @param password  password
     * @return Optional of the Java Web Token, empty otherwise
     */
    public Optional<String> signin(String username, String password) {
        Optional<String> token = Optional.empty();
        POCUser user = userRepo.findByUsername(username);
        if (user != null && user.isPresent(username)) {
            try {
                Authentication authObj = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                token = Optional.of(jwtProvider.createToken(username, user.getRoles()));
            } catch (AuthenticationException e){
                System.out.println("Login failed");
            }
        }
        
        return token;
    }


}
