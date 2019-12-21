package com.dmakarov.controller;

import static com.dmakarov.ApiPathsV1.LOGIN;
import static com.dmakarov.ApiPathsV1.ROOT;
import static org.apache.logging.log4j.util.Strings.isBlank;

import com.dmakarov.model.dto.JwtRequestDto;
import com.dmakarov.model.dto.JwtResponseDto;
import com.dmakarov.model.exception.ClientException;
import com.dmakarov.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ROOT)
@Profile("!test")
public class JwtAuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenService jwtTokenService;

  @PostMapping(LOGIN)
  JwtResponseDto createAuthenticationToken(@RequestBody JwtRequestDto authenticationRequest) {
    validate(authenticationRequest);
    log.info("Login request received, user {}", authenticationRequest.getEmail());

    Authentication authentication = authenticate(authenticationRequest.getEmail(),
        authenticationRequest.getPassword());

    String token = jwtTokenService.generateToken((UserDetails) authentication.getPrincipal());

    return JwtResponseDto.builder()
        .jwtToken(token)
        .build();
  }

  private Authentication authenticate(String username, String password) {
    try {
      return authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(username, password));
    } catch (DisabledException | BadCredentialsException | AccountExpiredException
        | UsernameNotFoundException e) {
      throw new ClientException(HttpStatus.BAD_REQUEST, "Authentication exception");
    }
  }

  private static void validate(JwtRequestDto authenticationRequest) {
    if (isBlank(authenticationRequest.getEmail()) || isBlank(authenticationRequest.getPassword())) {
      throw new ClientException(HttpStatus.BAD_REQUEST,
          "Some of required request attributes are empty [email,password]");
    }
  }

}
