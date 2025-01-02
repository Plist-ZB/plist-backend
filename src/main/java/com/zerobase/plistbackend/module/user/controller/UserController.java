package com.zerobase.plistbackend.module.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  // 테스트용 컨트롤러입니다

  @GetMapping("/")
  public String home() {
    return "Welcome to the home page!";
  }

  @GetMapping("/test")
  public String test() {
    return "Welcome to the test page!";
  }

  @GetMapping("/login")
  public String loginPage() {
    return """
               <html>
               <body>
                   <h1>Login Page</h1>
                   <a href="/oauth2/authorization/google">Login with Google</a>
               </body>
               </html>
               """;
  }

}