package com.zerobase.plistbackend.module.user.jwt;

import com.zerobase.plistbackend.module.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class JwtUtil {

  private final SecretKey secretKey;
  private final Long accessExpired;
  private final Long refreshExpired;
  private final UserRepository userRepository;

  public JwtUtil(
      @Value("${spring.jwt.secret}") String secret,
      @Value("${jwt.access}") Long accessExpired,
      @Value("${jwt.refresh}") Long refreshExpired,
      UserRepository userRepository) {
    this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
        SIG.HS256.key().build().getAlgorithm());
    this.accessExpired = accessExpired;
    this.refreshExpired = refreshExpired;
    this.userRepository = userRepository;
  }

  @Transactional(readOnly = true)
  public String createJwt(String category, String email, String role) {
    Date expiration = new Date(System.currentTimeMillis() + findExpired(category));

    return Jwts.builder()
        .claim("category", category)
        .claim("email", email)
        .claim("role", role)
        .claim("id", userRepository.findByUserEmail(email).getUserId())
        .claim("expired", expiration)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(expiration)
        .signWith(secretKey)
        .compact();
  }

  private Long findExpired(String category) {
    if (category.equals("access")) {
      return accessExpired;
    } else if (category.equals("refresh")) {
      return refreshExpired;
    }
    return null;
  }

  public String findCategory(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
        .get("category", String.class);
  }

  public String findEmail(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
        .get("email", String.class);
  }

  public String findRole(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
        .get("role", String.class);
  }

  public Boolean isExpired(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
        .getExpiration().before(new Date());
  }

  public Timestamp findExpiration(String token) {
    Date expired = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
        .get("expired", Date.class);
    return new Timestamp(expired.getTime());
  }

  public String findToken(HttpServletRequest request, String category) {
    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals(category)) {
        return cookie.getValue();
      }
    }
    return null;
  }


  public Cookie createCookie(String key, String value) {
    Cookie cookie = new Cookie(key, value);
    cookie.setMaxAge(findExpired(key).intValue() / 1000);
    cookie.setPath("/");
    cookie.setSecure(true);
    cookie.setHttpOnly(true);
    return cookie;
  }
}
