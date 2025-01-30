package com.zerobase.plistbackend.common.app.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  String customSchemeName = "Authorization";

  @Bean
  public OpenAPI openAPI() {

    Info info = new Info()
        .version("v1.0.0")
        .title("PLIST API")
        .description("사람과 음악을 잇다, 플리스트");

    Server localServer = new Server();
    localServer.setUrl("http://localhost:8080");
    localServer.setDescription("로컬 서버");

    Server prodServer = new Server();
    prodServer.setUrl("https://api.plist.shop");
    prodServer.setDescription("운영 서버");

    return new OpenAPI()
        .info(info)
//        .addServersItem(localServer)
//        .addServersItem(prodServer)
        .servers(List.of(localServer, prodServer))
        .addSecurityItem(new SecurityRequirement().addList(customSchemeName))
        .components(new Components().addSecuritySchemes(customSchemeName, createAPIKeyScheme()));
  }

  private SecurityScheme createAPIKeyScheme() {
    return new SecurityScheme()
        .type(Type.HTTP)
        .bearerFormat("JWT")
        .scheme("bearer")
        .in(SecurityScheme.In.HEADER)
        .name(customSchemeName);
  }
}