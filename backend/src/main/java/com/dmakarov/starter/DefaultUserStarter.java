package com.dmakarov.starter;

import com.dmakarov.dao.UserRepository;
import com.dmakarov.model.UserEntity;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Application runner which loads default admin user.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class DefaultUserStarter implements ApplicationRunner {

  private static final String ADMIN_EMAIL = "admin@admin.com";
  private static final String ADMIN_PASSWORD = "adminPassword";
  private final UserRepository repository;
  private final PasswordEncoder encoder;

  @Override
  public void run(ApplicationArguments args) {
    UserEntity adminUser = UserEntity.builder()
        .email(ADMIN_EMAIL)
        .password(encoder.encode(ADMIN_PASSWORD))
        .roles(Set.of("ROLE_ADMIN"))
        .build();

    repository.save(adminUser);
  }

}
