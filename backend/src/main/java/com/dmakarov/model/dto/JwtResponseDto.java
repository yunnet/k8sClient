package com.dmakarov.model.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class JwtResponseDto {
  String jwtToken;
}
