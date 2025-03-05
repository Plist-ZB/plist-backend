package com.zerobase.plistbackend.module.refresh.service;

import com.zerobase.plistbackend.module.refresh.dto.NewAccessResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface RefreshService {

  void addRefreshEntity(Long userId, String token);

  void checkRefresh(String refreshToken);

  NewAccessResponse newAccessToken(HttpServletRequest request, String refresh);

}
