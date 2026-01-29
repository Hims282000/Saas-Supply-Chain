package com.supplychain.tenant_service.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import org.apache.kafka.common.protocol.types.Field.Bool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.supplychain.tenant_service.Security.CustomerUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureAlgorithm;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(CustomerUserDetails userDetails){
        Map<String, Object> claims= new HashMap<>();
        claims.put("tenantId", userDetails.getTenantId().toString());
        claims.put("userId", userDetails.getId().toString());
        claims.put("role", userDetails.getRole());
        return createToken(claims,userDetails.getUsername());
    }

    private String createToken(Map<String,Object> claims , String subject){
        Date now= new Date();
        Date expiryDate=  new Date(now.getTime() + expiration);

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(now).setExpiration(expiryDate).signWith(getSigningKey(),SignatureAlgorithm.HS256).compact();
    }

    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims= extractAllClaims(token);
        return claimsResolver.apply(claims);

    }

    public Claims extractAllClaims(String token){
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();

    }

    public UUID extractTenantId(String token){
        Claims claims= extractAllClaims(token);
        return UUID.fromString(claims.get("tenantId", String.class));   
    }


    public UUID extractUserId(String token){
        Claims claims= extractAllClaims(token);
        return UUID.fromString(claims.get("userId", String.class));
    }

    public String extractRole(String token){
        Claims claims= extractAllClaims(token);
        return claims.get("role", String.class);
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
    
    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Boolean validateToken(String token, CustomerUserDetails userDetails){
        final String username= extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
