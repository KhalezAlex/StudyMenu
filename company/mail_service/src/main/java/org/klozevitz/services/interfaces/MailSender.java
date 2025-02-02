package org.klozevitz.services.interfaces;

import org.klozevitz.dto.MailParameters;

public interface MailSender {
    void send(MailParameters mailParameters);
}
