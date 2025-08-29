package com.seguridad.practica_seguridad.security;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.seguridad.practica_seguridad.security.filter.JwtTokenValidator;
import com.seguridad.practica_seguridad.service.userDetails.PersonaDetailServiceImpl;
import com.seguridad.practica_seguridad.util.JwtUtiles;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    
    private final JwtUtiles jwtUtiles;

    public SecurityConfig(JwtUtiles jwtUtiles) {
        this.jwtUtiles = jwtUtiles;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationProvider authenticationProvider)
            throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    // Endpoints públicos
                    http.requestMatchers(HttpMethod.POST, "/persona/login").permitAll();

                    // Endpoints protegidos
                    http.requestMatchers(HttpMethod.GET, "/persona/**").hasRole("USER");
                    http.requestMatchers(HttpMethod.POST, "/persona/**").hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.DELETE, "/persona/**").hasRole("ADMIN");
                    http.requestMatchers(HttpMethod.PUT, "/persona/**").hasRole("ADMIN");

                    http.anyRequest().authenticated(); // Cambiar denyAll() por authenticated()
                })
                .addFilterBefore(new JwtTokenValidator(jwtUtiles), BasicAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PersonaDetailServiceImpl userDetailService) {
        DaoAuthenticationProvider authentication = new DaoAuthenticationProvider();
        authentication.setPasswordEncoder(passwordEncoder());
        authentication.setUserDetailsService(userDetailService);
        return authentication;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
