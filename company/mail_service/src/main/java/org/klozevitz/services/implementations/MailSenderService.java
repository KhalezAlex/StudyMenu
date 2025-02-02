package org.klozevitz.services.implementations;

import lombok.RequiredArgsConstructor;
import org.klozevitz.dto.MailParameters;
import org.klozevitz.services.interfaces.MailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailSenderService implements MailSender {
    @Value("${spring.mail.username}")
    private String emailFrom;
    @Value("${service.activation.url}")
    private String activationUrl;
    private final JavaMailSender mailSender;


    @Override
    public void send(MailParameters mailParameters) {
        var subject = "Активация аккаунта";
        var messageBody = activationMessageBody(mailParameters);
        var emailTo = mailParameters.getEmailTo();

        var message = new SimpleMailMessage();
        message.setFrom(emailFrom);
        message.setTo(emailTo);
        message.setSubject(subject);
        message.setText(messageBody);

        mailSender.send(message);
    }

    private String activationMessageBody(MailParameters mailParameters) {
        var message = String.format("Пройдите по ссылке ниже для завершения регистрации:\n%s",
                activationUrl);
        return message.replace("{id}", mailParameters.getId());
    }
}
