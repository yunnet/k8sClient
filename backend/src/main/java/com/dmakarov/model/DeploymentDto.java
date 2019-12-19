package com.dmakarov.model;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeploymentDto {
  UUID deploymentId;
  String name;
  String image;
}
