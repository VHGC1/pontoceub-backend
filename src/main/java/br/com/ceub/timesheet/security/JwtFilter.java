package br.com.ceub.timesheet.security;

import br.com.ceub.timesheet.domain.dtos.AuthenticatedUserDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private static final String BEAR = "Bearer";
    private final JwtHelper jwtHelper;

    public JwtFilter(JwtHelper jwtHelper) { this.jwtHelper = jwtHelper; }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(Objects.isNull(authorizationHeader) || !authorizationHeader.startsWith(BEAR)) {
            filterChain.doFilter(request, response);
            return;
        }

        AuthenticatedUserDetails authenticatedUserDetails = jwtHelper.validateAndGetUser(authorizationHeader.replace(BEAR, ""));

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        authenticatedUserDetails,
                        null,
                        authenticatedUserDetails.roles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
