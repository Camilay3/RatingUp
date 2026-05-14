package com.quadcore.Ratingup.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {


    private final JavaMailSender mailSender;
    private static final String EMAIL_ORIGEM = "enviadorratingup@gmail.com";
    private static final String NOME_ENVIADOR = "RatingUp";

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendRecoverMail(String destiny, String token) {
        String conteudo = """
        <p>Olá,</p>
        <p>Recebemos uma solicitação para redefinir sua senha.</p>
        <p>Use o código abaixo para redefinir sua senha (expira em 30 minutos):</p>
        <h3>%s</h3>
        <p>Se não foi você, ignore este e-mail.</p>
        <br>
        <p>Equipe RatingUp</p>
    """.formatted(token);

        enviarEmail(destiny, "Recuperação de Senha - RatingUp", conteudo);
    }

    private void enviarEmail(String destino, String assunto, String conteudo) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(EMAIL_ORIGEM, NOME_ENVIADOR);
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