package com.quadcore.Ratingup.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {


    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String emailOrigem;

    @Value("${spring.mail.name}")
    private String nomeEnviador;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendRecoverMail(String destiny, String token) {
        Context context = new Context();
        context.setVariable("token", token);

        String conteudo = templateEngine.process("email/redefinir-senha", context);

        enviarEmail(destiny, "Recuperação de Senha - RatingUp", conteudo);
    }

    private void enviarEmail(String destino, String assunto, String conteudo) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(emailOrigem, nomeEnviador);
            helper.setTo(destino);
            helper.setSubject(assunto);
            helper.setText(conteudo, true); // true = HTML
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException("Erro ao enviar e-mail: " + e.getMessage());
        } catch (MailException e) {
            throw new RuntimeException("Erro ao enviar e-mail: " + e.getMessage());
        }
    }
}