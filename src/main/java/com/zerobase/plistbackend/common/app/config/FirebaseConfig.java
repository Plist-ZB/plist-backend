package com.zerobase.plistbackend.common.app.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class FirebaseConfig {

  @Bean
  public FirebaseMessaging firebaseMessaging() throws IOException {
    InputStream serviceAccount = new ClassPathResource(
        "firebase-service-account.json").getInputStream();

    FirebaseOptions options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

    FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
    return FirebaseMessaging.getInstance(firebaseApp);
  }
}