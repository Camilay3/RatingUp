package com.quadcore.Ratingup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Profile("prod")
public class EmailService {

    @Autowired(required = false)
    protected JavaMailSender mailSender;

    public void sendRecoverMail(String destiny, String token){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destiny);
        message.setSubject("Recuperação de senha");
        message.setText("Use o token abaixo para redefinir sua senha: \n\n" + token + "\n\nO token expira em 30 minutos");
        mailSender.send(message);
    }
}
