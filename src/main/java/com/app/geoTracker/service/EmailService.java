package com.app.geoTracker.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAlert(String deviceId) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo("delemacdele@gmail.com"); // AP’s email
        message.setSubject("Alert: " + deviceId + " has left home");
        message.setText("Your device " + deviceId + " has left the home area.");

        mailSender.send(message);
    }
}