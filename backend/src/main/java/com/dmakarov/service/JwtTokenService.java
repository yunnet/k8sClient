package com.dmakarov.service;

import io.jsonwebtoken.Claims;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtTokenService {

  /**
   * Retrieves user Id from jwt token.
   */
  String getUserIdFromToken(String token);

  /**
   * Retrieves granted authorities from jwt token.
   */
  List<GrantedAuthority> getAuthoritiesFromToken(String token);

  /**
   * Retrieves expiration date from jwt token.
   */
  Date getExpirationDateFromToken(String token);

  /**
   * Checks if token is already expired.
   */
  boolean isTokenExpired(String token);

  /**
   * Returns claim from jwt token.
   */
  <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver);

  /**
   * Generate token for user.
   */
  String generateToken(UserDetails userDetails);

  /**
   * Token validation.
   */
  UserDetails validateTokenAndGetUserDetails(String token);

}
