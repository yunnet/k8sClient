package com.dmakarov.dao;

import com.dmakarov.model.DeploymentEntity;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface DeploymentRepository extends CrudRepository<DeploymentEntity, Long> {
  DeploymentEntity findByNamespaceAndName(String namespace, String name);

  List<DeploymentEntity> findByNamespaceContaining(String namespace);
}
