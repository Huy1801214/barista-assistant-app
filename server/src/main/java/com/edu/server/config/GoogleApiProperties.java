package com.edu.server.config; // Hoặc một package config phù hợp

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "google") // Liên kết với các key có tiền tố "google"
public class GoogleApiProperties {

    private Client client = new Client();
    private String redirectUri;

    public static class Client {
        private String id;
        private String secret;

        // Getters and Setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getSecret() { return secret; }
        public void setSecret(String secret) { this.secret = secret; }
    }

    // Getters and Setters
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public String getRedirectUri() { return redirectUri; }
    public void setRedirectUri(String redirectUri) { this.redirectUri = redirectUri; }
}