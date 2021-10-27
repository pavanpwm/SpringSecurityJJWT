
HOW SPRING SECURITY WORKS WITH TYPICAL CORS REQUESTS
we go to a url

spring sec checks if the domain is allowed

if allowed  it takes to controller method

userService signin() method with uname and pass

	public Authenation signin(uname pass)
		return authenticationmanager.authenticate(new
			UsernamePasswordAuth..Token	(uname, pass)

the authmanager goes to loadUserByUsername method of UserDetailsService Class

		@Override
    		public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        		User user = userRepository.findByUsername(s).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User with name %s does not exist", s)));

       	 	return withUsername(user.getUsername())
        	    .password(user.getPassword())
            	.authorities(user.getRoles())
            	.accountExpired(false)
           	 .accountLocked(false)
            	.credentialsExpired(false)
          	  .disabled(false)
          	  .build();
    }

if username not exists then throws exception
else we inove a withUsername Static method to create a UserDetails Object with the user details from database

org.springframework.security.core.userdetails.User.withUsername() builder

then auth manager validates/authenticates login creds with this userDetails object data




++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

HOE JWT FILTER WITH SPRING SECURITY WORKS


two jwt classes

JwtTokenProvider
-creating and encoding jwt tokens
creates token given the username and list of roles
-username is set using setSubject meth
-roles can be added as key value pairs in payload//not necessary though


JwtTokenFilter 
-a web filter invoked before going to controller methods


WebSecurityConfiguration is added to JwtTokenFilter filter

RestController and UserDetailsService classes are now aware of jwt and role based security

when we sign in, we usually recieve jwt token in response

we add the filter in our security config class

------------

so when we run 
spring goes to the jwt http.addFilter jwtTokenFilter... in security config class

keeps running until we enter username and pass

when etnter
we hit to JwtTokenFilter class doFilte() method which checks for any authentication

then it goes to signin() method in REST controller

then we go to userService for signin()


----
after signing

control goes to JwtTokeProvider to create token string using user data and some configs

and the jwt token string is sent as response to browser

----------------------------------------------------------------------------

now when sending next request with authority i.e without needing to login again
we need send the jwt in header

key = Authorization
value = Bearer + " " + jwtTokenString

-----------------------------------------------------------------------------

