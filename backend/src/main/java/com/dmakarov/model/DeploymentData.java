package com.dmakarov.model;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeploymentData {
  String name;
  String namespace;
  int replicasCount;
  String image;
  List<String> commands;
  List<String> args;
  int port;
}
