package com.app.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.mongodb.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

	@Value("${jwt.secret.key}")
	private String secretKey;
	

	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		return Jwts
				.builder()
				.claims()
				.add(claims)
				.subject(username)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis()+3600*1000*24))
				.and()
				.signWith(getKey())
				.compact();
	}

	private SecretKey getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String extractUserName(String token) {
		if(token.startsWith("Bearer")) {
			token = token.substring(7);
		}
		return extractClaim(token,Claims::getSubject);
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimResolver){
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser()
				.verifyWith(getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String userName = extractUserName(token);
		return (userName.equals(userDetails.getUsername())& !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		return exttractExpiration(token).before(new Date());
	}

	private Date exttractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

}
