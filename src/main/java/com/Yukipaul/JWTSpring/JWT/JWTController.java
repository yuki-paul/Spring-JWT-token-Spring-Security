package com.Yukipaul.JWTSpring.JWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/authenticate")
@RequiredArgsConstructor
public class JWTController {
	
	private final JWTService jwtservice;
	
	private final AuthenticationManager authenticationManager;

	@PostMapping
	public String getTokenForAuthenticatedUser(@RequestBody JWTAuthRequest authRequest) {
		//checks whether the user is a valid user or not
		//only valid user should be created token
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
		if(authentication.isAuthenticated()) {
			return jwtservice.getGeneratedTokenForUser(authRequest.getUserName());

		}
		else {
			throw new UsernameNotFoundException("Invalid user credentials");
		}
		
}
}
