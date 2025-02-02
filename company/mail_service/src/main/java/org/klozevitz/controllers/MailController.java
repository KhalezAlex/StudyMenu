package org.klozevitz.controllers;

import lombok.RequiredArgsConstructor;
import org.klozevitz.dto.MailParameters;
import org.klozevitz.services.interfaces.MailSender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {
    private final MailSender mailSender;

    @PostMapping("/send")
    public ResponseEntity<?> sendActivationMail(@RequestBody MailParameters mailParameters) {
        mailSender.send(mailParameters);
        return ResponseEntity.ok().build();
    }
}
