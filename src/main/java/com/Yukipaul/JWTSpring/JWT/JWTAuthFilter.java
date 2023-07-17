package com.Yukipaul.JWTSpring.JWT;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Yukipaul.JWTSpring.security.LibraryUserDetails;
import com.Yukipaul.JWTSpring.security.LibraryUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
	@Autowired
	private LibraryUserDetailsService libuserDetailsService;
	@Autowired
	private  JWTService jwtservice;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		String authHeader = request.getHeader("Authorization");
		String jwtToken = null;
		String userName = null;
		if(authHeader!=null && authHeader.startsWith("Bearer ")) {
			jwtToken = authHeader.substring(7);
			userName = jwtservice.extractUserNameFromToken(jwtToken);
		}
		if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			UserDetails userdetails = libuserDetailsService.loadUserByUsername(userName);
			if(jwtservice.isValidToken(jwtToken, userdetails)) {
			var authToken = new UsernamePasswordAuthenticationToken( userdetails,null , userdetails.getAuthorities());
			authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
	}

}
