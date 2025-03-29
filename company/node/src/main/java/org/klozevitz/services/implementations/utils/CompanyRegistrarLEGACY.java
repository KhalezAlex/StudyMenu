package org.klozevitz.services.implementations.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.CompanyTelegramView;
import org.klozevitz.dto.MailParameters;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.logger.LoggerInfo;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.util.Registrar_LEGACY;
import org.klozevitz.utils.CryptoTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import static org.klozevitz.enitites.appUsers.enums.states.CompanyState.WAIT_FOR_EMAIL_CONFIRMATION_STATE;
import static org.klozevitz.enitites.appUsers.enums.views.CompanyView.EMAIL_CONFIRMATION_REQUEST_VIEW;


@Log4j
@RequiredArgsConstructor
public class CompanyRegistrarLEGACY implements Registrar_LEGACY {
    @Value("${fileService.service.mail.url}")
    private String mailServiceUrl;
    private final AppUserRepo appUserRepo;
    private final CryptoTool cryptoTool;
    private final CompanyTelegramView telegramView;
    private final LoggerInfo loggerInfo;


    @Override
    public SendMessage register(Update update, AppUser currentAppUser) {
        var email = update.getMessage().getText();

        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException e) {
            loggerInfo.LoggerErrorWrongEmail(update);
            return telegramView.wrongEmailNotificationView(update);
        }

        var userByEmail = appUserRepo.findByEmail(email);

        if (userByEmail.isPresent()) {
            loggerInfo.LoggerErrorAlreadyRegisteredEmail(update);
            return telegramView.alreadyRegisteredEmailNotificationView(update);
        }

        currentAppUser = setRegisteredCompanyParameters(currentAppUser, email);
        var responseFromMailServer = sendRequestToMailService(currentAppUser, email);

        if (responseFromMailServer.getStatusCode() != HttpStatus.OK) {
            setUnregisteredCompanyParameters(currentAppUser);
            return telegramView.emailCanNotBeSentNotificationView(update);
        }

        return telegramView.emailConfirmationRequestView(update);
    }

    private AppUser setRegisteredCompanyParameters(AppUser currentAppUser, String email) {
        currentAppUser.getCompany().setEmail(email);
        currentAppUser.getCompany().setState(WAIT_FOR_EMAIL_CONFIRMATION_STATE);
        currentAppUser.getCompany().setCurrentView(EMAIL_CONFIRMATION_REQUEST_VIEW);
        currentAppUser = appUserRepo.save(currentAppUser);

        return currentAppUser;
    }

    private void setUnregisteredCompanyParameters(AppUser currentAppUser) {
        currentAppUser.getCompany().setEmail(null);
        currentAppUser.getCompany().setCurrentView(null);
        appUserRepo.save(currentAppUser);
    }

    private ResponseEntity<String> sendRequestToMailService(AppUser currentAppUser, String email) {
        var cryptoUserId = cryptoTool.hashOf(currentAppUser.getId());
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        var mailParameters = MailParameters.builder()
                .id(cryptoUserId)
                .emailTo(email)
                .build();
        var request = new HttpEntity<>(mailParameters, headers);

        return restTemplate.exchange
                (
                        mailServiceUrl,
                        HttpMethod.POST,
                        request,
                        String.class
                );
    }
}
