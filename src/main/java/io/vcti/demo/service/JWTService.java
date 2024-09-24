package io.vcti.demo.service;


import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {
	
	//private final String SECRET="5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
	String secretKey;
	public JWTService()
	{
		try {
			KeyGenerator keyGen=KeyGenerator.getInstance("HmacSHA256");
			SecretKey sk=keyGen.generateKey();
			secretKey=Base64.getEncoder().encodeToString(sk.getEncoded());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String generateToken(String userName) {
		// TODO Auto-generated method stub
		Map<String, Object> claims=new HashMap<>();
		return Jwts.builder()
				.claims()
				.add(claims)
				.subject(userName)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis()+(60*60*10*60)))
				.and()
				.signWith(getSignKey())
				.compact();
		
	}

	private SecretKey getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	 private Boolean isTokenExpired(String token) {
	        return extractExpiration(token).before(new Date());
	    }

	    public Boolean validateToken(String token, UserDetails userDetails) {
	        final String username = extractUsername(token);
	        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	    }
	    public Date extractExpiration(String token) {
	        return extractClaim(token, Claims::getExpiration);
	    }
	
	  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	        final Claims claims = extractAllClaims(token);
	        return claimsResolver.apply(claims);
	    }

	    private Claims extractAllClaims(String token) {
	        return Jwts.parser()
	                .verifyWith(getSignKey())
	                .build()
	                .parseSignedClaims(token)
	                .getPayload();
	    }

}
