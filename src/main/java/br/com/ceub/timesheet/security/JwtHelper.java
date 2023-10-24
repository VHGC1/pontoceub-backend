package br.com.ceub.timesheet.security;

import br.com.ceub.timesheet.domain.dtos.AuthenticatedUserDetails;
import br.com.ceub.timesheet.domain.entities.Role;
import br.com.ceub.timesheet.domain.entities.User;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtHelper {
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration-time}")
    private Long expiration;

    public String createJwt(User user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("name", user.getName())
                .signWith(getSecretKey())
                .setExpiration(toExpirationDate())
                .claim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .compact();
    }

    public AuthenticatedUserDetails validateAndGetUser(String jwtToken) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(jwtToken).getBody();
        return new AuthenticatedUserDetails(
                Long.valueOf(claims.getSubject()),
                claims.get("name", String.class),
                claims.get("roles", List.class));
    }

    private Date toExpirationDate() {
        return Date.from(LocalDateTime.now().plus(expiration, ChronoUnit.MINUTES).atZone(ZoneId.systemDefault()).toInstant());
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
