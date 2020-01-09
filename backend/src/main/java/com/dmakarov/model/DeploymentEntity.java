package com.dmakarov.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "DEPLOYMENT")
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
public class DeploymentEntity {
  @Id
  @GeneratedValue
  private Long id;

  private String namespace;

  @Column(unique = true)
  private String name;

  private String image;

  private int replicasCount;

  @ElementCollection
  private List<String> commands;

  @ElementCollection
  private List<String> args;

  private int port;

  String status = "";
}
