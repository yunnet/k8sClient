package com.dmakarov.model;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Deployment {
  UUID deploymentId;
  String name;
  String image;
}
