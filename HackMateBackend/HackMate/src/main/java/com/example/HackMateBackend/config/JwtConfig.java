package com.example.HackMateBackend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfig {
    private String secret;
    private long expiration;
    private long refreshExpiration;

    public JwtConfig() {}

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public long getRefreshExpiration() {
        return refreshExpiration;
    }

    public void setRefreshExpiration(long refreshExpiration) {
        this.refreshExpiration = refreshExpiration;
    }
}


/*
The JwtConfig class is a Spring Boot configuration properties holder for your JWT (JSON Web Token)
 settings. It acts as a bridge between your external configuration files (like application.properties
 or application.yml) and your Java code, allowing you to manage sensitive and environment-specific JWT
 settings—such as the signing secret and token expiration times—cleanly, safely, and professionally.

With this setup, anywhere in your code where you inject JwtConfig, you can safely and easily access
 these values without hardcoding them or exposing them in source code. This is especially important
 for production security.


 */