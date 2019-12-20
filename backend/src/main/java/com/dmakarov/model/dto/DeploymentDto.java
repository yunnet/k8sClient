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
  String namespace;

  @NotEmpty(message = "Please provide a deployment name")
  String name;

  @NotNull(message = "Please provide a deployment replicas count")
  @Min(1)
  @Max(10)
  int replicasCount;

  @NotEmpty(message = "Please provide a deployment container image")
  String image;

  List<String> commands;

  List<String> args;

  @NotNull
  int port;
}
