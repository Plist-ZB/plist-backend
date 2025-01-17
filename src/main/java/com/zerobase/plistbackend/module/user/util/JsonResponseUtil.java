package com.zerobase.plistbackend.module.user.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.plistbackend.module.user.exception.UserException;
import com.zerobase.plistbackend.module.user.type.UserErrorStatus;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class JsonResponseUtil {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static void sendErrorResponse(HttpServletResponse response, HttpStatus status,
      String message) {

    response.setStatus(status.value());
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    Map<String, Object> responseBody = new HashMap<>();
    responseBody.put("status", status.value());
    responseBody.put("message", message);

    try {
      String jsonResponse = objectMapper.writeValueAsString(responseBody);
      response.getWriter().write(jsonResponse);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      throw new UserException(UserErrorStatus.SEND_ERROR_RESPONSE_IOEXCEPTION);
    }

  }

}
