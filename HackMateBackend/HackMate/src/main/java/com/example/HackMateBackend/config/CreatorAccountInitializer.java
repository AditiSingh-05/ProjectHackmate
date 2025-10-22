package com.example.HackMateBackend.config;

import com.example.HackMateBackend.data.entities.User;
import com.example.HackMateBackend.data.enums.Roles;
import com.example.HackMateBackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CreatorAccountInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${CREATOR_EMAIL:hackmatecreator@gmail.com}")
    private String creatorEmail;

    @Value("${CREATOR_PASSWORD:iamironman}")
    private String creatorPassword;

    @Bean
    public CommandLineRunner initCreatorAccount() {
        return args -> {
            log.info("Checking for creator account initialization...");

            if (!userRepository.existsByEmailIgnoreCase(creatorEmail)) {
                log.info("Creator account not found. Creating creator account with email: {}", creatorEmail);

                User creator = new User();
                creator.setEmail(creatorEmail.toLowerCase());
                creator.setPassword(passwordEncoder.encode(creatorPassword));
                creator.setRole(Roles.CREATOR);
                creator.setEmailVerified(true); // Creator is pre-verified
                creator.setProfileSetup(true); // Creator profile is pre-setup
                creator.setCreatedAt(LocalDateTime.now());
                creator.setUpdatedAt(LocalDateTime.now());

                userRepository.save(creator);
                log.info("✅ Creator account successfully created with CREATOR role");
            } else {
                log.info("Creator account already exists");

                // Ensure creator account has correct role and is verified
                userRepository.findByEmailIgnoreCase(creatorEmail).ifPresent(user -> {
                    boolean updated = false;

                    if (user.getRole() != Roles.CREATOR) {
                        user.setRole(Roles.CREATOR);
                        updated = true;
                        log.info("Updated creator account role to CREATOR");
                    }

                    if (!user.isEmailVerified()) {
                        user.setEmailVerified(true);
                        updated = true;
                        log.info("Verified creator account email");
                    }

                    if (!user.isProfileSetup()) {
                        user.setProfileSetup(true);
                        updated = true;
                        log.info("Set creator account profile as complete");
                    }

                    if (updated) {
                        userRepository.save(user);
                        log.info("✅ Creator account updated successfully");
                    }
                });
            }
        };
    }
}

