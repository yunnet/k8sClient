package com.dmakarov.service.impl;

import static java.time.Instant.now;
import static java.util.stream.Collectors.toList;

import com.dmakarov.config.properties.JwtProperties;
import com.dmakarov.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {
  private static final String DUMMY_PASSWORD = "DUMMY_PASSWORD";

  private final JwtProperties properties;

  @Override
  public String getUserIdFromToken(String token) {
    return getClaimFromToken(token, Claims::getSubject);
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
    return getClaimFromToken(token, claims -> {
      List<String> authorities = claims.get("authorities", List.class);

      return authorities.stream()
          .map(SimpleGrantedAuthority::new)
          .collect(toList());
    });
  }

  @Override
  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  @Override
  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Check if the token has expired.
   */
  @Override
  public boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  /**
   * Generates token from UserDetails, note that user password is not available in UserDetails
   * since it is erased by Spring Security.
   */
  @Override
  public String generateToken(UserDetails userDetails) {
    // convert roles to strings in advance because JWT stores everything as text in JSON.
    List<String> authorities = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::toString)
        .collect(toList());
    Map<String, Object> claims = Map.of("authorities", authorities);

    return Jwts.builder()
        .setSubject(userDetails.getUsername())
        .addClaims(claims)
        .setExpiration(Date.from(now().plus(properties.getTtl())))
        .signWith(SignatureAlgorithm.HS512, properties.getSecret()).compact();
  }

  /**
   * For retrieving any information from token we will need the secret key.
   */
  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parser().setSigningKey(properties.getSecret()).parseClaimsJws(token).getBody();
  }

  @Override
  public UserDetails validateTokenAndGetUserDetails(String token) {
    return User.builder()
        .username(getUserIdFromToken(token))
        .password(DUMMY_PASSWORD)
        .authorities(getAuthoritiesFromToken(token))
        .build();
  }

}
