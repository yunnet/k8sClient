package com.dmakarov.controller;

import static com.dmakarov.ApiPathsV1.DEPLOYMENTS;
import static com.dmakarov.ApiPathsV1.ROOT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dmakarov.model.Deployment;
import com.dmakarov.model.DeploymentDto;
import com.dmakarov.service.DeploymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Arrays;
import java.util.UUID;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest
@AutoConfigureMockMvc
class DeploymentsControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private DeploymentService service;

  @Test
  void createDeployment() throws Exception {
    UUID deploymentId = UUID.randomUUID();
    String name = RandomString.make(10);
    String image = RandomString.make(10);
    DeploymentDto deploymentDto = DeploymentDto.builder()
        .deploymentId(deploymentId)
        .name(name)
        .image(image)
        .build();
    Deployment deployment = Deployment.builder()
        .deploymentId(deploymentId)
        .name(name)
        .image(image)
        .build();

    when(service.createDeployment(any(DeploymentDto.class)))
        .thenReturn(deployment);

    this.mockMvc
        .perform(
            post(ROOT + DEPLOYMENTS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(deploymentDto))
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.deploymentId")
            .value(deploymentDto.getDeploymentId().toString()))
        .andExpect(jsonPath("$.name").value(deploymentDto.getName()))
        .andExpect(jsonPath("$.image").value(deploymentDto.getImage()));
  }

  @Test
  void getDeployment() throws Exception {
    UUID deploymentId = UUID.randomUUID();
    String name = RandomString.make(10);
    String image = RandomString.make(10);
    Deployment deployment = Deployment.builder()
        .deploymentId(deploymentId)
        .name(name)
        .image(image)
        .build();

    when(service.getDeployment(any(UUID.class))).thenReturn(deployment);

    this.mockMvc
        .perform(
            get(ROOT + DEPLOYMENTS + "/{deploymentId}", deploymentId)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.deploymentId")
            .value(deployment.getDeploymentId().toString()))
        .andExpect(jsonPath("$.name").value(deployment.getName()))
        .andExpect(jsonPath("$.image").value(deployment.getImage()));
  }

  @Test
  void getDeployments() throws Exception {
    UUID deploymentId = UUID.randomUUID();
    String name = RandomString.make(10);
    String image = RandomString.make(10);
    Deployment firstDeployment = Deployment.builder()
        .deploymentId(deploymentId)
        .name(name)
        .image(image)
        .build();
    Deployment secondDeployment = Deployment.builder()
        .deploymentId(deploymentId)
        .name(name)
        .image(image)
        .build();
    when(service.getDeployments())
        .thenReturn(Arrays.asList(firstDeployment, secondDeployment));

    this.mockMvc
        .perform(
            get(ROOT + DEPLOYMENTS)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].deploymentId")
            .value(firstDeployment.getDeploymentId().toString()))
        .andExpect(jsonPath("$[0].name").value(firstDeployment.getName()))
        .andExpect(jsonPath("$[1].deploymentId")
            .value(secondDeployment.getDeploymentId().toString()))
        .andExpect(jsonPath("$[1].name").value(secondDeployment.getName()));
  }

  /**
   * Serializes Object to Json string representation.
   *
   * @param obj serialized object
   * @return json representation
   */
  static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
          .writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
