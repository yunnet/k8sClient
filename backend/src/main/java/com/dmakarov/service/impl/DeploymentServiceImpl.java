package com.dmakarov.service.impl;

import com.dmakarov.model.Deployment;
import com.dmakarov.model.DeploymentDto;
import com.dmakarov.service.DeploymentService;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeploymentServiceImpl implements DeploymentService {

  @Override
  public Deployment createDeployment(DeploymentDto deploymentDto) {
    UUID deploymentId = UUID.randomUUID();

    return Deployment.builder()
        .deploymentId(deploymentId)
        .name(deploymentDto.getName())
        .image(deploymentDto.getImage())
        .build();
  }

  @Override
  public List<Deployment> getDeployments() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Deployment getDeployment(UUID deploymentId) {
    throw new UnsupportedOperationException();
  }
}
