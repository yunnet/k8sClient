package com.dmakarov.dao;

import com.dmakarov.model.DeploymentEntity;
import org.springframework.data.repository.CrudRepository;

public interface DeploymentRepository extends CrudRepository<DeploymentEntity, Long> {
  DeploymentEntity findByNamespaceAndName(String namespace, String name);
}
