package com.example.hmsUser.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtService
{

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437cjhs9dfhuwehfuosdaajb";

    public String generateToken(String email,String role)
    {
        return createToken(role, email );
    }

    private String createToken(String role, String email)
    {
        return Jwts.builder()
                .claim("role", role)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // Token valid for 60 minutes
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return Jwts.parser()
                .setSigningKey(String.valueOf(SECRET))
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Long extractId(String token) {
        // Assuming the patient ID is stored in the JWT payload under the key "patientId"
        return Long.parseLong(extractClaim(token, claims -> claims.get("patientId", String.class)));
    }

//    public Long extractId(String token) {
//        // Extract patient ID from the token (assuming you store it in the JWT payload)
//        return Long.parseLong(extractClaim(token, Claims::getSubject)); // Example of extracting ID
//    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        //final String role = extractRole(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}