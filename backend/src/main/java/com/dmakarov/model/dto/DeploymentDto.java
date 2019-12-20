package com.dmakarov.model.dto;

import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeploymentDto {
  /**
   * Deployment Namespace.
   */
  String namespace;

  /**
   * Deployment name. Unique field.
   */
  @NotEmpty(message = "Please provide a deployment name")
  String name;

  /**
   * Deployment replicas count.
   */
  @NotNull(message = "Please provide a deployment replicas count")
  @Min(1)
  @Max(10)
  int replicasCount;

  /**
   * Deployment container image name.
   */
  @NotEmpty(message = "Please provide a deployment container image")
  String image;

  /**
   * Deployment container commands.
   */
  List<String> commands;

  /**
   * Deployment container args.
   */
  List<String> args;

  /**
   * Deployment container port.
   */
  @NotNull
  @Min(1)
  int port;

  /**
   * Deployment status. Shows "Available"/"Requested" replicas. Example: 2/2
   */
  String status;
}
