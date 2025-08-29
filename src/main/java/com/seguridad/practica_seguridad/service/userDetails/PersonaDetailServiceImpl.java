package com.seguridad.practica_seguridad.service.userDetails;

import java.net.Authenticator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.seguridad.practica_seguridad.model.dtos.AuthCreateUserRequest;
import com.seguridad.practica_seguridad.model.dtos.AuthResponse;
import com.seguridad.practica_seguridad.model.dtos.LoginRequest;
import com.seguridad.practica_seguridad.model.entities.PersonaEntity;
import com.seguridad.practica_seguridad.model.entities.RoleEntity;
import com.seguridad.practica_seguridad.repository.PersonaRepository;
import com.seguridad.practica_seguridad.repository.RolRepository;
import com.seguridad.practica_seguridad.util.JwtUtiles;


@Service
public class PersonaDetailServiceImpl  implements UserDetailsService{
   
    private final PersonaRepository usuarioRepo;
     private final JwtUtiles jwtUtiles;
    private final RolRepository rolRepo;
    
    public PersonaDetailServiceImpl(PersonaRepository usuarioRepo, JwtUtiles jwtUtiles, RolRepository rolRepo) {
        this.usuarioRepo = usuarioRepo;
        this.jwtUtiles = jwtUtiles;
        this.rolRepo = rolRepo;
    }

   

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PersonaEntity usuario=usuarioRepo.findByName(username).orElseThrow(null);
        List <GrantedAuthority> authorities = new ArrayList<>();
        for (RoleEntity rol : usuario.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_".concat(rol.getName().toString())));
        }
        
        return new User(usuario.getName(), usuario.getPassword(), authorities);
    }

    public AuthResponse loginUser(LoginRequest authLoginRequest){
        String username=authLoginRequest.username();
        String password=authLoginRequest.password();
        
        //SI QUIERO USAR EL CORREO TAMBIEN PARA CREAR EL TOKEN
        /*PersonaEntity usuario = usuarioRepo.findByName(username).orElseThrow(); 
        String accessToken = jwtUtiles.createToken(authentication, usuario.getEmail()); */

        Authentication authentication=this.authentication(username,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
       
        String accessToken = jwtUtiles.createToken(authentication);
        
        AuthResponse authResponse=new AuthResponse(username,"user logged succes",accessToken,true);
        return authResponse;
    }

    public Authentication authentication(String username,String password){
        UserDetails userDetails=this.loadUserByUsername(username);
        if (userDetails==null) {
            throw new BadCredentialsException("INVALID USERNAME OR PASSWORD");
        }
        if (!password.equals(userDetails.getPassword())) {
         throw new BadCredentialsException("INVALID  PASSWORD");

        }
        return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
    }

    public AuthResponse createUser(AuthCreateUserRequest authCreateUserRequest){
        String username=authCreateUserRequest.username();
        String password=authCreateUserRequest.password();
        String surname=authCreateUserRequest.surname();
        String email=authCreateUserRequest.email();
        List<String> roleRequest=authCreateUserRequest.roleRequest().roleListName();
        Set<RoleEntity>roles=rolRepo.findRoleEntitiesByERoleIn(roleRequest).stream().collect(Collectors.toSet());
        if (roles.isEmpty()) {
            throw new IllegalArgumentException("the role specified does no exist");
        }
        PersonaEntity user=new PersonaEntity().builder()
        .name(username)
        .email(email)
        .password(password)
        .surname(surname)
        .roles(List.copyOf(roles))
        .build();
        PersonaEntity userCreated=usuarioRepo.save(user);
        List <GrantedAuthority> authorities = new ArrayList<>();
        userCreated.getRoles().forEach(role->authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getName().toString()))));
       // SecurityContext context=SecurityContextHolder.getContext();
        Authentication authentication=new UsernamePasswordAuthenticationToken(userCreated.getName(),userCreated.getPassword(),authorities);

        String accessToken=jwtUtiles.createToken(authentication);
        AuthResponse authResponse=new AuthResponse(userCreated.getName(),"user created successfully",accessToken,true);
        return authResponse;

    }
}
