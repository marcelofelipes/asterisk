package com.asterisk.backend.service;

import com.asterisk.backend.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class EmailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;

    @Autowired
    public EmailService(final JavaMailSender javaMailSender, final SpringTemplateEngine springTemplateEngine) {
        this.javaMailSender = javaMailSender;
        this.springTemplateEngine = springTemplateEngine;
    }

    @Async
    public void sendRegisterConfirmationEmail(final User user, final String confirmationCode) {

        // setup
        try {
            final MimeMessage message = this.javaMailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            // set context
            final Context context = new Context();
            context.setVariable("username", user.getUsername());
            context.setVariable("code", confirmationCode);

            // get html
            final String html = this.springTemplateEngine.process("register-confirmation-email", context);

            // construct mail
            helper.setTo(user.getEmail());
            helper.setFrom("no-reply@example.com");
            helper.setSubject("Complete the registration of your account");
            helper.setText(html, true);

            this.javaMailSender.send(message);
        } catch (final MessagingException e) {
            LOGGER.error("Error sending email {}", e.getMessage());
        }
    }

    @Async
    public void sendForgotPasswordEmail(final User user, final UUID forgotPasswordTokenId) {
        try {
            // setup
            final MimeMessage message = this.javaMailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            // set context
            final String passwordResetLink = String.format("http://localhost:4200/forgot-password/reset?_fpid=%s",
                    forgotPasswordTokenId);
            final Context context = new Context();
            context.setVariable("username", user.getUsername());
            context.setVariable("link", passwordResetLink);

            // get html
            final String html = this.springTemplateEngine.process("forgot-password-email", context);

            // construct mail
            helper.setTo(user.getEmail());
            helper.setFrom("no-reply@example.com");
            helper.setSubject("Reset password request");
            helper.setText(html, true);

            this.javaMailSender.send(message);
        } catch (final MessagingException e) {
            LOGGER.error("Error sending email {}", e.getMessage());
        }
    }
}
