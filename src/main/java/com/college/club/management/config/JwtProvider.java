package com.college.club.management.config;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.college.club.management.exception.UserNotFound;
import com.college.club.management.models.JwtConstants;
import com.college.club.management.services.RootAdminServices;
import com.mongodb.Function;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
	RootAdminServices rootAdminServices;
	public JwtProvider(	RootAdminServices rootAdminServices) {
		this.rootAdminServices = rootAdminServices;
	}
	
	static SecretKey key = Keys.hmacShaKeyFor(JwtConstants.SECRET_KEY.getBytes());
	public String generateToken(String username) throws UserNotFound {
        Map<String, Object> claims = Map.of("roles", rootAdminServices.findUserByUsername(username).getRoles()); 
        return createToken(claims, username);
        }

	private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) 
                .signWith(key)
                .compact();
    }
	
	public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class); // Assuming roles are stored as a List of strings in the JWT claims
    }

    
    private Claims extractAllClaims(String token) {
        return Jwts
				.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
    }

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }
    private Boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
	
	
}

