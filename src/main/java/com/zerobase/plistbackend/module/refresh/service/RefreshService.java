package com.zerobase.plistbackend.module.refresh.service;

import com.zerobase.plistbackend.module.refresh.dto.NewAccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

public interface RefreshService {

  void addRefreshEntity(String email, String token, Timestamp expired);

  void checkRefresh(String refreshToken);

  NewAccessResponse newAccessToken(HttpServletRequest request);

  void refreshCleanup();

}
