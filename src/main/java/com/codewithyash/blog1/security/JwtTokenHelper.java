package com.codewithyash.blog1.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.function.*;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenHelper {
	
	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
	
	private final Key secret = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	
	//retrieve username from jwt token 
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	//retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration );
	}
	
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	// for retrieving any information from token we will need the secret key
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser()
				.setSigningKey(secret)
				.build()
				.parseClaimsJws(token)
				.getBody();	
	}
	
	//cheak if the token has expired
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	
	//generate token for user 
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}
	
	//while creating the token - 
	//1. define claims of the token, like Ussuer, Expiration, Subject, and the ID
	//2. sign the JWT using the HS512 algorithm and secret key.
	//3. Accoring to jws Compect Serialization(
	//   Compection of the jwt to a URL- safe string
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		
		 return Jwts.builder()
	                .setClaims(claims)
	                .setSubject(subject)
	                .setIssuedAt(new Date(System.currentTimeMillis()))  // ✅ Fixed: Correct `new Date()`
	                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)) // ✅ Fixed: Correct expiration time
	                .signWith(secret)  // ✅ Fixed: `signWith` method call
	                .compact();	}
	
	//validate token
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));		
	}

}
