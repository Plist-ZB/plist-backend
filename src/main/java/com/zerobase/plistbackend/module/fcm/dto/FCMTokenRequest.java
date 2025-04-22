package com.zerobase.plistbackend.module.fcm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FCMTokenRequest {

  @NotBlank(message = "토큰 값은 필수입니다.")
  private String token;
}
