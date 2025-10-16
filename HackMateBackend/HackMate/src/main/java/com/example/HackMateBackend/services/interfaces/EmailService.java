package com.example.HackMateBackend.services.interfaces;



public interface EmailService {


    void sendEmailVerification(String to, String verificationToken);


    void sendPasswordResetEmail(String to, String resetToken);


    void sendWelcomeEmail(String to, String name);


    void sendNotificationEmail(String to, String subject, String message);
}

//now i want to create profile setup