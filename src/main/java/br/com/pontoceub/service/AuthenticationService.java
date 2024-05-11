package br.com.pontoceub.service;

import br.com.pontoceub.Security.JwtService;
import br.com.pontoceub.Security.UserAuthenticated;
import br.com.pontoceub.Security.UserDetailsServiceImpl;
import br.com.pontoceub.domain.dto.TokenDTO;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthenticationService(JwtService jwtService, PasswordEncoder passwordEncoder, UserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    public TokenDTO authenticate(String email, String password) {
        UserDetails user = userDetailsService.loadUserByUsername(email);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setToken(jwtService.generateToken((UserAuthenticated) user));

        return tokenDTO;
    }
}
