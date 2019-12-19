package com.dmakarov.deployment;

import com.dmakarov.model.DeploymentData;
import io.fabric8.kubernetes.api.model.ContainerPortBuilder;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class DeploymentFabric {
  public static Function<DeploymentData, Deployment> deployment = (deploymentData)
      -> new DeploymentBuilder()
      .withApiVersion("apps/v1beta2")
      .withNewMetadata()
      .withName(deploymentData.getName())
      .withNamespace(deploymentData.getNamespace())
      .withLabels(
          Map.of(
              "app", deploymentData.getName())
      )
      .endMetadata()
      .withNewSpec()
      .withNewSelector()
      .addToMatchLabels(
          Map.of(
              "app", deploymentData.getName())
      )
      .endSelector()
      .withReplicas(deploymentData.getReplicasCount())
      .withNewTemplate()
      .withNewMetadata()
      .withLabels(
          Map.of(
              "app", deploymentData.getName())
      )
      .endMetadata()
      .withNewSpec()
      .addNewContainer()
      .withName("container-name")
      .withImage(deploymentData.getImage())
      .withImagePullPolicy("Always")
      .withCommand(
          deploymentData.getCommands()
      )
      .withArgs(
          deploymentData.getArgs()
      )
      .withPorts(
          List.of(
              new ContainerPortBuilder()
                  .withName("node-port")
                  .withContainerPort(deploymentData.getPort())
                  .build()
          )
      )
      .endContainer()
      .withRestartPolicy("Always")
      .endSpec()
      .endTemplate()
      .endSpec()
      .build();

}
