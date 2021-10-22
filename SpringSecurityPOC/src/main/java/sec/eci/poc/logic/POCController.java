package sec.eci.poc.logic;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;

import sec.eci.poc.jwt.JwtProvider;
import sec.eci.poc.user.POCUserService;


@RestController
public class POCController {

	@Autowired
	POCUserService userService;
	@Autowired
	JwtProvider jwtProvider;
	
	@PostMapping("/login")
    public Optional<String> login(@RequestBody LoginEntity creds) {
       return userService.signin(creds.getUsername(), creds.getPassword());
    }
	
	@GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String checkForAdminAccess(){
        return "You are allowed for admin";
    }
	
	@GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String checkForUserAccess(){
        return "You are allowed for user";
    }
	
	@GetMapping("/customer")
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public String checkForCustomerAccess(){
        return "You are allowed for customer";
    }
	
	
	
}
