package com.example.HackMateBackend.services.implementations;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.example.HackMateBackend.services.interfaces.EmailService;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    @Override
    public void sendEmailVerification(String to, String verificationToken) {
        log.info("AuthenticationVerification: Starting email verification process for: {}", to);
        log.info("AuthenticationVerification: From email configured as: {}", fromEmail);
        log.info("AuthenticationVerification: Base URL configured as: {}", baseUrl);
        log.info("AuthenticationVerification: Verification token: {}", verificationToken);

        try {
            log.info("AuthenticationVerification: Creating SimpleMailMessage object");
            SimpleMailMessage message = new SimpleMailMessage();

            log.info("AuthenticationVerification: Setting from email: {}", fromEmail);
            message.setFrom(fromEmail);

            log.info("AuthenticationVerification: Setting to email: {}", to);
            message.setTo(to);

            log.info("AuthenticationVerification: Setting subject");
            message.setSubject("HackMate - Verify Your Email");

            String emailBody = buildEmailVerificationMessage(verificationToken);
            log.info("AuthenticationVerification: Email body created, length: {}", emailBody.length());
            log.debug("AuthenticationVerification: Email body content: {}", emailBody);
            message.setText(emailBody);

            log.info("AuthenticationVerification: Attempting to send email via JavaMailSender");
            log.info("AuthenticationVerification: JavaMailSender instance: {}", mailSender.getClass().getSimpleName());

            mailSender.send(message);

            log.info("AuthenticationVerification: ✅ Email verification sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("AuthenticationVerification: ❌ Failed to send email verification to: {}", to);
            log.error("AuthenticationVerification: Exception type: {}", e.getClass().getSimpleName());
            log.error("AuthenticationVerification: Exception message: {}", e.getMessage());
            log.error("AuthenticationVerification: Full stack trace:", e);
            throw new RuntimeException("Failed to send verification email: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendPasswordResetEmail(String to, String resetToken) {
        log.info("AuthenticationVerification: Sending password reset email to: {}", to);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject("HackMate - Reset Your Password");
            message.setText(buildPasswordResetMessage(resetToken));

            mailSender.send(message);
            log.info("AuthenticationVerification: Password reset email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("AuthenticationVerification: Failed to send password reset email to: {}", to, e);
            throw new RuntimeException("Failed to send password reset email");
        }
    }

    @Override
    public void sendWelcomeEmail(String to, String name) {
        log.info("AuthenticationVerification: Sending welcome email to: {}", to);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject("Welcome to HackMate!");
            message.setText(buildWelcomeMessage(name));

            mailSender.send(message);
            log.info("AuthenticationVerification: Welcome email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("AuthenticationVerification: Failed to send welcome email to: {}", to, e);
            throw new RuntimeException("Failed to send welcome email");
        }
    }

    @Override
    public void sendNotificationEmail(String to, String subject, String messageBody) {
        log.info("AuthenticationVerification: Sending notification email to: {}", to);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(messageBody);

            mailSender.send(message);
            log.info("AuthenticationVerification: Notification email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("AuthenticationVerification: Failed to send notification email to: {}", to, e);
            throw new RuntimeException("Failed to send notification email");
        }
    }

    private String buildEmailVerificationMessage(String token) {
        log.info("AuthenticationVerification: Building email verification message with token: {}", token);

        String verificationUrl = baseUrl + "/api/auth/verify-email?token=" + token;
        log.info("AuthenticationVerification: Verification URL created: {}", verificationUrl);

        String message = String.format(
                "Welcome to HackMate!\n\n" +
                        "Please click the following link to verify your email address:\n" +
                        "%s\n\n" +
                        "Or copy and paste this token in the verification form: %s\n\n" +
                        "This link will expire in 24 hours.\n\n" +
                        "If you did not create an account with HackMate, please ignore this email.\n\n" +
                        "Best regards,\n" +
                        "The HackMate Team",
                verificationUrl,
                token
        );

        log.info("AuthenticationVerification: Email message built successfully, character count: {}", message.length());
        return message;
    }

    private String buildPasswordResetMessage(String token) {
        String resetUrl = baseUrl + "/api/auth/reset-password?token=" + token;
        return String.format(
                "Hello,\n\n" +
                        "You have requested to reset your password for your HackMate account.\n\n" +
                        "Please click the following link to reset your password:\n" +
                        "%s\n\n" +
                        "Or use this reset token: %s\n\n" +
                        "This link will expire in 24 hours.\n\n" +
                        "If you did not request a password reset, please ignore this email.\n\n" +
                        "Best regards,\n" +
                        "The HackMate Team",
                resetUrl,
                token
        );
    }

    private String buildWelcomeMessage(String name) {
        return String.format(
                "Welcome to HackMate, %s!\n\n" +
                        "Thank you for joining our community of developers and hackathon enthusiasts.\n\n" +
                        "You can now:\n" +
                        "- Discover exciting hackathons\n" +
                        "- Connect with fellow developers\n" +
                        "- Build amazing projects\n\n" +
                        "Get started by exploring hackathons at: %s\n\n" +
                        "Happy coding!\n" +
                        "The HackMate Team",
                name != null ? name : "Developer",
                baseUrl
        );
    }
}
