package sec.eci.poc.user;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import sec.eci.poc.jwt.JwtProvider;



@Component
public class POCUserDetailService implements UserDetailsService {

	@Autowired
    JwtProvider jwtProvider;
	@Autowired
	POCUserRepo userRepository;
	
	
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        POCUser user = userRepository.findByUsername(username);
        if (!user.getUsername().equals(username)) {
        	new UsernameNotFoundException(String.format("User with name %s does not exist", username));
		}else {
			 return org.springframework.security.core.userdetails.User.withUserDetails(new POCUserDetails(user)).build();
					 
//					 withUsername(user.getUsername())
//			            .password(user.getPassword())
//			            .authorities(user.getRoles())
//			            .accountExpired(false)
//			            .accountLocked(false)
//			            .credentialsExpired(false)
//			            .disabled(false)
//			            .build();
		}
        
		return null;    
    }

    /**
     * Extract username and roles from a validated jwt string.
     * @param jwtToken jwt string
     * @return UserDetails if valid, Empty otherwise
     */
    public Optional<UserDetails> loadUserByJwtToken(String jwtToken) {
        if (jwtProvider.isValidToken(jwtToken)) {
 
            return Optional.of(
             org.springframework.security.core.userdetails.User.withUsername(jwtProvider.getUsername(jwtToken))
                .authorities(jwtProvider.getRoles(jwtToken))
                .password("") //token does not have password but field may not be empty
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build());
        }
        return Optional.empty();
    }

	/**
     * Extract the username from the JWT then lookup the user in the database.
     *
     * @param jwtToken
     * @return
     */
    public Optional<UserDetails> loadUserByJwtTokenAndDatabase(String jwtToken) {
        if (jwtProvider.isValidToken(jwtToken)) {
            return Optional.of(loadUserByUsername(jwtProvider.getUsername(jwtToken)));
        } else {
            return Optional.empty();
        }
    }
    

}
