package com.dmakarov.controller;

import static com.dmakarov.ApiPathsV1.DEPLOYMENT;
import static com.dmakarov.ApiPathsV1.NAMESPACE;
import static com.dmakarov.ApiPathsV1.ROOT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.dmakarov.model.dto.DeploymentDto;
import com.dmakarov.service.DeploymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Arrays;
import java.util.Optional;
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
    String namespace = RandomString.make(10);
    String name = RandomString.make(10);
    String image = RandomString.make(10);
    DeploymentDto deploymentDto = DeploymentDto.builder()
        .namespace(namespace)
        .name(name)
        .image(image)
        .build();

    when(service.createDeployment(anyString(), any(DeploymentDto.class)))
        .thenReturn(deploymentDto);

    this.mockMvc
        .perform(
            post(ROOT + NAMESPACE + "/{namespace}", namespace)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(deploymentDto))
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(deploymentDto.getName()))
        .andExpect(jsonPath("$.image").value(deploymentDto.getImage()));
  }

  @Test
  void getDeployment() throws Exception {
    String namespace = RandomString.make(10);
    String name = RandomString.make(10);
    String image = RandomString.make(10);
    DeploymentDto deploymentDto = DeploymentDto.builder()
        .namespace(namespace)
        .name(name)
        .image(image)
        .build();

    when(service.getDeployment(anyString(), anyString())).thenReturn(Optional.of(deploymentDto));

    this.mockMvc
        .perform(
            get(ROOT + NAMESPACE + "/{namespace}" + DEPLOYMENT + "/{deploymentName}",
                namespace, name)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(deploymentDto.getName()))
        .andExpect(jsonPath("$.image").value(deploymentDto.getImage()));
  }

  @Test
  void getDeployment_notFound() throws Exception {
    when(service.getDeployment(anyString(), anyString())).thenReturn(Optional.empty());

    this.mockMvc
        .perform(
            get(ROOT + NAMESPACE + "/{namespace}" + DEPLOYMENT + "/{deploymentName}",
                RandomString.make(10), RandomString.make(10))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  void getDeployments() throws Exception {
    String namespace = RandomString.make(10);
    String name = RandomString.make(10);
    String image = RandomString.make(10);
    DeploymentDto firstDeployment = DeploymentDto.builder()
        .namespace(namespace)
        .name(name)
        .image(image)
        .build();
    DeploymentDto secondDeployment = DeploymentDto.builder()
        .namespace(namespace)
        .name(name)
        .image(image)
        .build();
    when(service.getDeployments(anyString()))
        .thenReturn(Arrays.asList(firstDeployment, secondDeployment));

    this.mockMvc
        .perform(
            get(ROOT + NAMESPACE + "/{namespace}", namespace)
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value(firstDeployment.getName()))
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
