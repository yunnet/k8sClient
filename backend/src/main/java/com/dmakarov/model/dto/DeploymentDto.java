package com.dmakarov.model.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeploymentDto {
  String namespace;
  String name;
  String image;
}
