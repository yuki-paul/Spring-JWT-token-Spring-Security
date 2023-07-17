package com.Yukipaul.JWTSpring.JWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.Yukipaul.JWTSpring.User.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class JWTService {
	
	
	@Value("${spring.jwt.secret}")
	private String JWT_SECRET;
	
	@Value("${spring.jwt.jwtExpirationTime}")
	private int JWT_EXPIRATION_TIME;
	
	private final UserRepository userRepo;

	
	public String getGeneratedTokenForUser(String userName) {
		Map<String , Object> claims = new HashMap<>();
		return generateToken(claims, userName);
	}
	
	public String generateToken(Map<String , Object> claims, String userName) {
		return Jwts
				.builder()
				.setClaims(claims)
				.setSubject(userName)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+ JWT_EXPIRATION_TIME))
				.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
	}

	private Key getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	private <T> T extractClaim(String token , Function<Claims,T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSignKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public String extractUserNameFromToken(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	public Date extractExpirationDateFromToken(String token) {
		return extractClaim(token, Claims :: getExpiration);
	}
	public boolean isValidToken(String token , UserDetails userdetails) {
		final String userName = extractUserNameFromToken(token);
		return (userName.equals(userdetails.getUsername()) && !isTokenExpired(token) );
	}
	
	public boolean isTokenExpired(String token) {
		return extractExpirationDateFromToken(token).before(new Date());
	}
}
