package com.dmakarov.controller;

import static com.dmakarov.ApiPathsV1.DEPLOYMENTS;
import static com.dmakarov.ApiPathsV1.ROOT;

import com.dmakarov.model.Deployment;
import com.dmakarov.model.DeploymentDto;
import com.dmakarov.service.DeploymentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(ROOT)
public class DeploymentsController {

  private final DeploymentService service;

  @PostMapping(DEPLOYMENTS)
  ResponseEntity<Deployment> createDeployment(@RequestBody DeploymentDto deploymentDto) {
    log.info("Create deployment request received, deployment {}", deploymentDto);

    Deployment deployment = service.createDeployment(deploymentDto);

    return ResponseEntity.ok().body(deployment);
  }

  @GetMapping(DEPLOYMENTS + "/{deploymentId}")
  ResponseEntity<Deployment> getDeployment(@PathVariable UUID deploymentId) {
    log.info("Get deployment request received, deploymentId {}", deploymentId);

    Deployment deployment = service.getDeployment(deploymentId);

    return ResponseEntity.ok().body(deployment);
  }

  @GetMapping(DEPLOYMENTS)
  ResponseEntity<List<Deployment>> getDeployments() {
    log.info("Get deployments request received");

    List<Deployment> deployment = service.getDeployments();

    return ResponseEntity.ok().body(deployment);
  }
}
