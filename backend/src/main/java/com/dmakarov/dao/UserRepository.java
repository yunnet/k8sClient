package com.dmakarov.dao;

import com.dmakarov.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
  UserEntity findByEmail(String email);
}
