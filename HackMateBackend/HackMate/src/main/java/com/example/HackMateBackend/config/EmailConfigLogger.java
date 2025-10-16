package com.example.HackMateBackend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EmailConfigLogger implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(EmailConfigLogger.class);

    @Value("${spring.mail.host}")
    private String mailHost;

    @Value("${spring.mail.port}")
    private String mailPort;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String smtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String starttlsEnable;

    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private String starttlsRequired;

    @Override
    public void run(String... args) {
        log.info("AuthenticationVerification: ⚙️ Email Configuration Summary:");
        log.info("AuthenticationVerification: Mail Host: {}", mailHost);
        log.info("AuthenticationVerification: Mail Port: {}", mailPort);
        log.info("AuthenticationVerification: Mail Username: {}", mailUsername);
        log.info("AuthenticationVerification: Mail Password: {} (masked)",
                mailPassword != null && !mailPassword.isEmpty() ? "*".repeat(mailPassword.length()) : "NOT SET");
        log.info("AuthenticationVerification: SMTP Auth: {}", smtpAuth);
        log.info("AuthenticationVerification: STARTTLS Enable: {}", starttlsEnable);
        log.info("AuthenticationVerification: STARTTLS Required: {}", starttlsRequired);
        log.info("AuthenticationVerification: 🚀 Application ready to send emails!");
    }
}
