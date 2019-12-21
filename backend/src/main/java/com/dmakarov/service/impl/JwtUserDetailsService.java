package com.dmakarov.service.impl;

import static java.util.stream.Collectors.toList;

import com.dmakarov.dao.UserRepository;
import com.dmakarov.model.UserEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
  private final UserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity user = repository.findByEmail(username);

    if (user == null) {
      throw new UsernameNotFoundException(
          String.format("User Entity not found. User %s", username));
    }

    List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
        .map(SimpleGrantedAuthority::new)
        .collect(toList());

    log.info("User authenticated: {} {}", user.getEmail(), authorities);

    return User.withUsername(user.getEmail())
        .password(user.getPassword())
        .authorities(authorities)
        .build();
  }
}
