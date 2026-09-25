package com.quadcore.Ratingup.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {


    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final JavaMailSender fallbackMailSender;

    @Value("${spring.mail.username}")
    private String emailOrigem;

    @Value("${spring.mail.name}")
    private String nomeEnviador;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        
        // Configura o Mailpit como servidor local de fallback
        JavaMailSenderImpl fallback = new JavaMailSenderImpl();
        fallback.setHost("mailpit"); // Usa o nome do container do docker-compose
        fallback.setPort(1025);
        this.fallbackMailSender = fallback;
    }

    public void sendRecoverMail(String destiny, String token) {
        Context context = new Context();
        context.setVariable("token", token);

        String conteudo = templateEngine.process("email/redefinir-senha", context);

        enviarEmail(destiny, "Recuperação de Senha - RatingUp", conteudo);
    }

    private void enviarEmail(String destino, String assunto, String conteudo) {
        try {
            send(this.mailSender, destino, assunto, conteudo);
        } catch (Exception e) {
            log.warn("Falha ao enviar pelo SMTP principal. Tentando servidor de Fallback local (Mailpit)", e);
            
            try {
                send(this.fallbackMailSender, destino, assunto, conteudo);
                log.info("E-mail enviado com sucesso via Fallback (Mailpit)!");
            } catch (Exception fallbackError) {
                throw new RuntimeException("Erro ao enviar e-mail em ambos os servidores: " + fallbackError.getMessage());
            }
        }
    }

    private void send(JavaMailSender sender, String destino, String assunto, String conteudo) throws MessagingException, UnsupportedEncodingException, MailException {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(emailOrigem, nomeEnviador);
        helper.setTo(destino);
        helper.setSubject(assunto);
        helper.setText(conteudo, true); // true = HTML
        sender.send(message);
    }
}