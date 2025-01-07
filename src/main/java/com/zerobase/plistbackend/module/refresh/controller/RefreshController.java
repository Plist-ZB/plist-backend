package com.zerobase.plistbackend.module.refresh.controller;

import com.zerobase.plistbackend.module.refresh.dto.NewAccessResponse;
import com.zerobase.plistbackend.module.refresh.service.RefreshService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api")
@Tag(name = "Jwt Token API", description = "Access token 관리")
public class RefreshController {

  private final RefreshService refreshService;

  @PostMapping("/auth/access")
  public ResponseEntity<NewAccessResponse> newAccess(HttpServletRequest request) {
    return ResponseEntity.status(201).body(refreshService.newAccessToken(request));
  }
}
