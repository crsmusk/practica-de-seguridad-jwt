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
     
    //llave secreta para la firma
    @Value("${jwt.secret}")
    private String secretKey;
    //variable que dice quien esta emitiendo el token 
    @Value("${security.jwt.user.generator}")
    private String userGenerator;

    public String createToken(Authentication authentication) {
        //se crea una firma  para verificar integridad y autenticidad
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        String userName = authentication.getPrincipal().toString();

        //extraemos los roles y los ponemos en un string todo junto separado por comas
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        //se crea el token con los datos necesarios para validar al usuario
        String jwtToken = JWT.create()
                //identifica al usuario

                .withSubject(userName)
                // roles y permisos para saber que clase de coasas puede hacer el usuario
                .withClaim("authorities", authorities)
                 
                //emisor quien emitio el token 
                .withIssuer(this.userGenerator)

                // Cuando se creo el token
                .withIssuedAt(new Date())
                 
                //asi habria que poner para usar el email para crear el token
                // .withClaim("email",email)  

                //cuando expira el token
                .withExpiresAt(new Date(System.currentTimeMillis() + 1800000))

                //identificador unico del token
                .withJWTId(UUID.randomUUID().toString())

                //cuando empieza a servir el token
                .withNotBefore(new Date(System.currentTimeMillis()))

                //firma 
                .sign(algorithm);
        return jwtToken;

    }
    //validacion del token
    public DecodedJWT validateToken(String token){
        try{
          // verifica que el token fue firmado con la clave secreta correcta
           Algorithm algorithm =Algorithm.HMAC256(this.secretKey);
           // Crea el verificador con las reglas de validación
           JWTVerifier verifier= JWT.require(algorithm)
           //verifica que el token fue emitido por la entidad esperada
           .withIssuer(this.userGenerator)
           .build();
           //hace toda la validación: firma, algoritmo, issuer, expiración
          DecodedJWT decodedJWT=verifier.verify(token);
          return decodedJWT;
        }catch(JWTVerificationException exception){
           throw new JWTVerificationException("token invalid");
        }
    }
     //devuelve el nombre de usuario
    public String extructUsername(DecodedJWT decodedJWT){
        return decodedJWT.getSubject().toString();
    }
     //devulve un claim expecifio 
    public Claim getExpecificClaim(DecodedJWT decodedJWT,String claimName){
      return decodedJWT.getClaim(claimName);
    }
    //devuelve todos los claims
    public Map<String,Claim> returnAllClaims(DecodedJWT decodedJWT){
      return decodedJWT.getClaims();
    }

     //asi seria para extraer el email si lo usaramos
    /*public String extructEmail(DecodedJWT decodedJWT){
        return decodedJWT.getClaim("email").asString(); 
    } */
}