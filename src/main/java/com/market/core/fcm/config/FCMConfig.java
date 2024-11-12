package com.market.core.fcm.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;

@Configuration
public class FCMConfig {

    @Value("${google.firebase.secret-key-path}")
    private String GOOGLE_APPLICATION_CREDENTIALS;
    @Value("${google.firebase.projectId}")
    private String GOOGLE_PROJECT_ID;

    /**
     * Firebase SDK 초기화
     */
    @PostConstruct
    private void init() {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream(GOOGLE_APPLICATION_CREDENTIALS);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId(GOOGLE_PROJECT_ID)
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Bean
    public FirebaseMessaging getInstance() {
        return FirebaseMessaging.getInstance();
    }
}
