package com.groupfour.bankingapp.Security;

import com.groupfour.bankingapp.Models.UserType;
import io.jsonwebtoken.*;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    @Value("${application.token.validity}")
    private long validityInMilliseconds;

    private final JwtKeyProvider jwtKeyProvider;
    @Value("${application.token.secret}")
    private String secret;

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null) {
            System.err.println("Authorization header is missing");
            return null;
        }

        if (!bearerToken.startsWith("Bearer ")) {
            System.err.println("Bearer prefix is missing in the token");
            return null;
        }

        return bearerToken.substring(7); // Remove "Bearer " prefix
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtKeyProvider.getPrivateKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("Token validation error: " + e.getMessage());
            return false;
        }
    }

    public JwtTokenProvider(JwtKeyProvider jwtKeyProvider) {
        this.jwtKeyProvider = jwtKeyProvider;
    }

    public String createToken(String firstName, String lastName, String email, Long userId, Long customerId, UserType type, Boolean isApproved) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
        claims.put("firstName", firstName);
        claims.put("lastName", lastName);
        claims.put("customerId", customerId);
        claims.put("email", email);
        claims.put("auth", type.name());
        claims.put("approved", isApproved);

        Date now = new Date();
        Date expiration = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(jwtKeyProvider.getPrivateKey())
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(jwtKeyProvider.getPrivateKey()).build().parseClaimsJws(token);
        String userID = claims.getBody().getSubject();
        String authority = claims.getBody().get("auth", String.class);
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(authority));

        System.out.println("Authorities: " + authorities);
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(userID, "", authorities);
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }
}
