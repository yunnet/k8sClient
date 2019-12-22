package com.dmakarov.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dmakarov.config.properties.JwtProperties;
import com.dmakarov.service.JwtTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

class JwtTokenServiceImplTest {
  private static final UserDetails USER = createNewUser();
  private JwtProperties properties;
  private JwtTokenService service;

  @BeforeEach
  void setUp() {
    properties = new JwtProperties();
    properties.setSecret("secret");
    properties.setTtl(Duration.ofHours(1L));
    service = new JwtTokenServiceImpl(properties);
  }

  @Test
  void roundTrip() {
    String token = service.generateToken(USER);
    UserDetails userFromToken = service.validateTokenAndGetUserDetails(token);

    assertThat(userFromToken).isEqualTo(USER);
  }

  @Test
  void expiredToken() {
    properties.setTtl(Duration.ZERO);

    String token = service.generateToken(USER);

    assertThrows(ExpiredJwtException.class, () -> service.validateTokenAndGetUserDetails(token));
  }

  private static UserDetails createNewUser() {
    return User.withUsername("John")
        .password("asd123")
        .authorities("ROLE_USER", "ROLE_ADMIN")
        .build();
  }

}
