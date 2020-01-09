package com.dmakarov;

import static com.dmakarov.deployment.DeploymentFactory.DEPLOYMENT_FUNCTION;

import com.dmakarov.model.DeploymentEntity;
import com.dmakarov.model.dto.DeploymentDto;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.api.model.apps.DeploymentStatus;
import java.util.List;

/**
 * Deployment helper functions.
 */
public class DeploymentUtils {

  /**
   * Returns new deployment dto, with provided port.
   */
  public static DeploymentDto getDeploymentDto(int port) {
    return DeploymentDto.builder()
        .namespace("namespace")
        .name("name")
        .image("image")
        .replicasCount(1)
        .port(port)
        .build();
  }

  /**
   * Returns new deployment dto.
   */
  public static DeploymentDto getDeploymentDto() {
    return DeploymentDto.builder()
        .namespace("namespace")
        .name("name")
        .image("image")
        .replicasCount(1)
        .build();
  }

  /**
   * Returns DeploymentEntity from DeploymentDto.
   */
  public static DeploymentEntity getDeploymentEntity(DeploymentDto deploymentDto, String status) {
    return DeploymentEntity.builder()
        .namespace(deploymentDto.getNamespace())
        .name(deploymentDto.getName())
        .image(deploymentDto.getImage())
        .replicasCount(deploymentDto.getReplicasCount())
        .commands(deploymentDto.getCommands())
        .args(deploymentDto.getArgs())
        .port(deploymentDto.getPort())
        .status(status)
        .build();
  }

  /**
   * Returns new deployment.
   */
  public static Deployment getDeployment(DeploymentEntity deploymentEntity) {
    Deployment deployment = DEPLOYMENT_FUNCTION.apply(deploymentEntity);
    DeploymentStatus deploymentStatus = new DeploymentStatus();
    deploymentStatus.setReadyReplicas(1);
    deploymentStatus.setReplicas(1);
    deployment.setStatus(deploymentStatus);
    return deployment;
  }

  /**
   * Returns deployments list.
   */
  public static DeploymentList getDeploymentList(DeploymentEntity deploymentEntity) {
    Deployment deployment = getDeployment(deploymentEntity);
    DeploymentList deploymentList = new DeploymentList();
    deploymentList.setItems(List.of(deployment));
    return deploymentList;
  }
}
