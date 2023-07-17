package com.Yukipaul.JWTSpring.security;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.Yukipaul.JWTSpring.User.User;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class LibraryUserDetails implements UserDetails {
	
	private String userName;
	
	private String password;
	
	private List<GrantedAuthority> authorities;
	
	public LibraryUserDetails(User user) {
		this.userName = user.getEmail();
		this.password = user.getPassword();
		this.authorities = Arrays
				.stream(user.getRole()
				.split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
