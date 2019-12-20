package com.dmakarov.controller;

import static com.dmakarov.ApiPathsV1.DEPLOYMENT;
import static com.dmakarov.ApiPathsV1.NAMESPACE;
import static com.dmakarov.ApiPathsV1.ROOT;

import com.dmakarov.model.dto.DeploymentDto;
import com.dmakarov.service.DeploymentService;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(ROOT)
public class DeploymentsController {

  private final DeploymentService service;

  @PostMapping(NAMESPACE + "/{namespace}" + DEPLOYMENT)
  ResponseEntity<DeploymentDto> createDeployment(@PathVariable String namespace,
      @Valid @RequestBody DeploymentDto deploymentDto) {
    log.info("Create deployment request received, deployment {}", deploymentDto);

    DeploymentDto deployment = service.createDeployment(namespace, deploymentDto);

    return ResponseEntity.ok().body(deployment);
  }

  @PutMapping(NAMESPACE + "/{namespace}" + DEPLOYMENT + "/{deploymentName}")
  ResponseEntity<DeploymentDto> updateDeployment(@PathVariable String namespace,
      @PathVariable String deploymentName, @Valid @RequestBody DeploymentDto deploymentDto) {
    log.info("Update deployment request received, deployment {}", deploymentDto);

    DeploymentDto deployment = service.updateDeployment(namespace, deploymentName, deploymentDto);

    return ResponseEntity.ok().body(deployment);
  }

  @GetMapping(NAMESPACE + "/{namespace}" + DEPLOYMENT + "/{deploymentName}")
  ResponseEntity<DeploymentDto> getDeployment(@PathVariable String namespace,
      @PathVariable String deploymentName) {
    log.info("Get deployment request received, deployment {}", deploymentName);

    Optional<DeploymentDto> deployment = service.getDeployment(namespace, deploymentName);

    return deployment.isPresent()
        ? ResponseEntity.ok().body(deployment.get())
        : ResponseEntity.notFound().build();
  }

  @GetMapping(NAMESPACE + "/{namespace}" + DEPLOYMENT)
  ResponseEntity<List<DeploymentDto>> getDeployments(@PathVariable String namespace) {
    log.info("Get deployments request received");

    List<DeploymentDto> deployment = service.getDeployments(namespace);

    return ResponseEntity.ok().body(deployment);
  }

  @DeleteMapping(NAMESPACE + "/{namespace}" + DEPLOYMENT + "/{deploymentName}")
  ResponseEntity deleteDeployment(@PathVariable String namespace,
      @PathVariable String deploymentName) {
    log.info("Delete deployment request received, deployment {}", deploymentName);

    service.deleteDeployment(namespace, deploymentName);

    return ResponseEntity.ok().build();
  }
}
