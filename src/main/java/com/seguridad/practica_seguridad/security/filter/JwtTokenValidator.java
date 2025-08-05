package com.seguridad.practica_seguridad.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.seguridad.practica_seguridad.util.JwtUtiles;

import ch.qos.logback.core.joran.conditional.IfAction;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

public class JwtTokenValidator extends OncePerRequestFilter {

    private JwtUtiles utiles;

    public JwtTokenValidator(JwtUtiles jwtUtiles) {
        this.utiles = jwtUtiles;
    }

    @Override
protected void doFilterInternal(@NonNull HttpServletRequest request,
                               @NonNull HttpServletResponse response, 
                               @NonNull FilterChain filterChain)
        throws ServletException, IOException {
    
     String jwtToken=request.getHeader(HttpHeaders.AUTHORIZATION);
            if(jwtToken !=null){
                jwtToken=jwtToken.substring(7);

                DecodedJWT DecodeJwt=utiles.validateToken(jwtToken);
                 
                //String email = utiles.extractEmail(decodedJWT); 
                String username=utiles.extructUsername(DecodeJwt);
                String stringAuthorities=utiles.getExpecificClaim(DecodeJwt, "authorities").asString();

                Collection<? extends GrantedAuthority>authorities=AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities);
                
                SecurityContext context=SecurityContextHolder.getContext();
                Authentication authentication=new UsernamePasswordAuthenticationToken(username, null,authorities);
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);

                // request.setAttribute("userEmail", email);
            }
    
    // CRÍTICO: Siempre continuar la cadena
    filterChain.doFilter(request, response);
}
}
