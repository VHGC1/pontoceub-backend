package br.com.pontoceub.service;

import br.com.pontoceub.Security.UserAuthenticated;
import br.com.pontoceub.domain.dto.UserDTO;
import br.com.pontoceub.domain.entities.User;
import br.com.pontoceub.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService extends AbstractDTOService<UserDTO, User, Long, UserRepository> {

    private final JwtDecoder decoder;
    private final HttpServletRequest request;
    private final PasswordEncoder passwordEncoder;

    public UserService(HttpServletRequest request, UserRepository userRepository, JwtDecoder decoder, PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.request = request;
        this.decoder = decoder;
        this.passwordEncoder = passwordEncoder;
    }

    public Long getUserIdFromRequest() {
        String token = extractTokenFromRequest(request);

        if (token != null) {
            Jwt claims = decoder.decode(token);

            return extractUserIdFromClaims(claims);
        }
        return null;
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }

    private Long extractUserIdFromClaims(Jwt claims) {
        return Long.valueOf(claims.getSubject()); // Assuming user ID is stored as subject in the token
    }

    @Override
    public UserDTO save(UserDTO userDTO) {
        if(findByEmail(userDTO.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já existente!");
        }

        User user = new User();

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        return entityToDTO(repo.save(user));
    }

    public Optional<User> findByEmail(String email) {
        return repo.findByEmail(email);
    }

}
