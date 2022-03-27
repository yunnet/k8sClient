package com.dmakarov.model.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class JwtRequestDto {
  String username;
  String password;
}
