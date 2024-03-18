package uk.ac.warwick.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uk.ac.warwick.model.Account;
import uk.ac.warwick.service.AccountService;

import java.io.IOException;
import java.util.Objects;

import static uk.ac.warwick.utility.JwtUtility.extractUsername;
import static uk.ac.warwick.utility.JwtUtility.validateToken;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AccountService accountService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String token = request.getHeader("Authorization");

        if (Objects.nonNull(token) && token.startsWith("Bearer ")) {
            var jwt = token.substring(7);
            var username = extractUsername(jwt);
            if (username != null) {
                var account = accountService.loadUserByUsername(username);
                if (validateToken(jwt, account)) {
                    var authToken = new UsernamePasswordAuthenticationToken(account, null, account.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
