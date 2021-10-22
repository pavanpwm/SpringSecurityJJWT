package sec.eci.poc.jwt;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider{

    private final String ROLES_KEY = "roles";

    private String secretKey;
    private long validityInMilliseconds;
    
    public JwtProvider() {
    }

    @Autowired
    public JwtProvider(@Value("${security.jwt.token.secret-key}") String secretKey,
                       @Value("${security.jwt.token.expiration}")long validityInMilliseconds) {

        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.validityInMilliseconds = validityInMilliseconds;
    }

    
    
    // Create JWT string given a user
    public String createToken(String username, ArrayList<String> roles) {
        //Add the username to the payload as subject
        Claims claims = Jwts.claims().setSubject(username);
        //Convert roles to Spring Security SimpleGrantedAuthority objects,
        //Add to Simple Granted Authority objects to claims
        claims.put(ROLES_KEY, roles);
        //Build the Token
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    //Validate the JWT String @param token JWT string @return true if valid, false otherwise
    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Get the username from the token string  @param token jwt @return username
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody().getSubject();
    }
    

    //Get the roles from the token string  @param token jwt @return username
    public List<GrantedAuthority> getRoles(String token) {
        List<String>  roleClaims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get(ROLES_KEY, List.class);
        List<GrantedAuthority> authorities = new ArrayList<>();
        roleClaims.forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
        return authorities;
    }
}