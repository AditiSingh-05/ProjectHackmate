package com.example.HackMateBackend.services.implementations;


import com.example.HackMateBackend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.HackMateBackend.data.entities.User;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailService.class);

    @Autowired
    private UserRepository userRepository;


    //It should fetch the user from the database, and convert the user entity into a UserDetails object—which
    // Spring Security will then use to check the password and assign roles.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> {
                            logger.error("User not found with email: {}", email);

                            return new UsernameNotFoundException("User not found with email: " + email);

                        }
                        );
        logger.debug("User found: {}, Role: {}", user.getEmail(),user.getRole());
        return UserPrincipal.create(user);


    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        logger.debug("Loading user by ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", id);
                    return new UsernameNotFoundException("User not found with id: " + id);
                });

        return UserPrincipal.create(user);
    }


    public static class UserPrincipal implements UserDetails {
        private Long id;
        private String email;
        private String password;
        private Collection<? extends GrantedAuthority> authorities;
        private boolean emailVerified;
        private boolean profileSetup;

        private UserPrincipal(Long id, String email, String password,
                              Collection<? extends GrantedAuthority> authorities,
                              boolean emailVerified, boolean profileSetup) {
            this.id = id;
            this.email = email;
            this.password = password;
            this.authorities = authorities;
            this.emailVerified = emailVerified;
            this.profileSetup = profileSetup;
        }

        public static UserPrincipal create(User user) {
            Collection<GrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
            );

            return new UserPrincipal(
                    user.getId(),
                    user.getEmail(),
                    user.getPassword(),
                    authorities,
                    user.isEmailVerified(),
                    user.isProfileSetup()
            );
        }

        // UserDetails interface methods
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public String getPassword() {
            return password;
        }

        @Override
        public String getUsername() {
            return email;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return emailVerified; // Only verified users can login
        }

        // Custom getters
        public Long getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public boolean isEmailVerified() {
            return emailVerified;
        }

        public boolean isProfileSetup() {
            return profileSetup;
        }
    }


}

//UserPrincipal.create(User user): Static factory method to convert a User entity into a UserPrincipal,
// including mapping roles to GrantedAuthority (e.g., ROLE_USER, ROLE_ADMIN).
//getUsername/getPassword/getAuthorities etc.: Required by UserDetails interface. These methods tell
// Spring Security how to get the user’s credentials and roles.
//isEnabled(): Returns emailVerified—so only users who have verified their email can log in (professional
// security best practice).