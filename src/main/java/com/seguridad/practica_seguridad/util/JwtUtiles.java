package com.seguridad.practica_seguridad.util;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import ch.qos.logback.classic.Logger;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JwtUtiles {

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    public String createToken(Authentication authentication) {

        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        String userName = authentication.getPrincipal().toString();
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String jwtToken = JWT.create()
                .withSubject(userName)
                .withClaim("authorities", authorities)
                .withIssuer(this.userGenerator)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1800000))
                .withJWTId(UUID.randomUUID().toString())
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);
        return jwtToken;

    }
    
    public DecodedJWT validateToken(String token){
        try{
           Algorithm algorithm =Algorithm.HMAC256(this.secretKey);
           JWTVerifier verifier= JWT.require(algorithm)
           .withIssuer(this.userGenerator)
           .build();
          DecodedJWT decodedJWT=verifier.verify(token);
          return decodedJWT;
        }catch(JWTVerificationException exception){
           throw new JWTVerificationException("token invalid");
        }
    }
    
    public String extructUsername(DecodedJWT decodedJWT){
        return decodedJWT.getSubject().toString();
    }
    public Claim getExpecificClaim(DecodedJWT decodedJWT,String claimName){
      return decodedJWT.getClaim(claimName);
    }
    public Map<String,Claim> returnAllClaims(DecodedJWT decodedJWT){
      return decodedJWT.getClaims();
    }
}