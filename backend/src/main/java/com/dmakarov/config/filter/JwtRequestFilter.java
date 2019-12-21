package com.dmakarov.config.filter;

import static com.dmakarov.config.SecurityConfig.PUBLIC_API_URLS;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.dmakarov.dao.UserRepository;
import com.dmakarov.model.UserEntity;
import com.dmakarov.service.JwtTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnMissingClass("org.springframework.boot.test.context.SpringBootTest")
public class JwtRequestFilter extends OncePerRequestFilter {

  private final JwtTokenService jwtTokenService;
  private final UserRepository repository;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return PUBLIC_API_URLS.matches(request);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    String requestTokenHeader = request.getHeader("Authorization");

    if (requestTokenHeader == null) {
      log.trace("Authorization header not found, request {}", request.getServletPath());
      chain.doFilter(request, response);
    } else if (!requestTokenHeader.startsWith("Bearer ")) {
      log.warn("Expected JWT token that begins with Bearer string. Actual token is {}",
          requestTokenHeader);
      response.sendError(UNAUTHORIZED.value(), "JWT Token does not begin with Bearer String");
    } else {
      // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
      String jwtToken = requestTokenHeader.substring(7);
      try {
        UserDetails userDetails = jwtTokenService.validateTokenAndGetUserDetails(jwtToken);
        //if user was already deleted
        String email = userDetails.getUsername();
        UserEntity user = repository.findByEmail(email);
        if (user == null) {
          log.warn("User with id {} not found", email);
          response.sendError(HttpStatus.UNAUTHORIZED.value(),
              String.format("User with id %s not found", email));
          return;
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        usernamePasswordAuthenticationToken
            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      } catch (IllegalArgumentException e) {
        log.warn("Unable to get JWT Token", e);
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Unable to get JWT Token");
        return;
      } catch (ExpiredJwtException e) {
        log.info("JWT Token has expired", e);
        response.sendError(UNAUTHORIZED.value(), "JWT Token has expired");
        return;
      } catch (SignatureException e) {
        log.info("Incorrect JWT Token signature", e);
        response.sendError(UNAUTHORIZED.value(), "Incorrect JWT Token signature");
        return;
      }
      chain.doFilter(request, response);
    }
  }

}
