package com.Yukipaul.JWTSpring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.Yukipaul.JWTSpring.Exception.UserNotFoundException;
import com.Yukipaul.JWTSpring.User.UserRepository;
import com.Yukipaul.JWTSpring.User.UserService;

import lombok.RequiredArgsConstructor;

@Component
public class LibraryUserDetailsService implements UserDetailsService {

	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		return userRepository
				.findByEmail(username)
				.map(LibraryUserDetails:: new)
				.orElseThrow(() -> new UserNotFoundException("user not found"));
	}

}
