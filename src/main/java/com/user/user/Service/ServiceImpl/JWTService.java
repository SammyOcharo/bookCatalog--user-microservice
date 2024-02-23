package com.user.user.Service.ServiceImpl;

import com.user.user.Entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
@SuppressWarnings("unused")
public class JWTService {

    public final String secret = "jihbfiaubiybequeyvufqyobdhoyfeyqbfuoebfyyeboqubefbubreuiqboerygqybog";


    public String generateToken(User user){

        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 24*60*60*1000))
                .signWith(SignWithKey())
                .compact();
    }

    private SecretKey SignWithKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(SignWithKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver){

        Claims claims = extractAllClaims(token);

        return  resolver.apply(claims);
    }

    public  String extractUsername(String token){

        return extractClaim(token, Claims::getSubject);
    }

    public boolean isValidToken(String token, UserDetails user){
         String username = extractUsername(token);

         return (username.equals(user.getUsername())&& isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
