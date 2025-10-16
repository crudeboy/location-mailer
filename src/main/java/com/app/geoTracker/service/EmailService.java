package com.app.geoTracker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;


    @Async
    public void sendAlert(String deviceId) {
        try {
            log.info("Sending device change mail for {}", deviceId);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo("delemacdele@gmail.com"); // AP’s email
            message.setSubject("Alert: " + deviceId + " has left home");
            message.setText("Your device " + deviceId + " has left the home area.");

            mailSender.send(message);
            log.info("Mail sent for {}", deviceId);
        }catch (MailException mex){
            log.error("Error while sending mail {}", mex.getMessage());
        }
    }
}