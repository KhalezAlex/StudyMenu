package org.klozevitz.services.implementations;

import lombok.RequiredArgsConstructor;
import org.klozevitz.CompanyTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.enums.views.CompanyView;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.Activator;
import org.klozevitz.services.interfaces.AnswerProducer;
import org.klozevitz.utils.CryptoTool;
import org.springframework.stereotype.Service;

import static org.klozevitz.enitites.appUsers.enums.states.CompanyState.BASIC_STATE;

@Service
@RequiredArgsConstructor
public class ActivatorService implements Activator {
    private final AppUserRepo appUserRepo;
    private final CryptoTool cryptoTool;
    private final CompanyTelegramView telegramView;
    private final AnswerProducer answerProducer;

    @Override
    public boolean activate(String cryptoUserId) {
        var userId = cryptoTool.idOf(cryptoUserId);
        var optionalUser = appUserRepo.findById(userId);

        if (optionalUser.isEmpty()) {
            return false;
        }

        var user = optionalUser.get();

        produceNotificationMessageToCompanyAnswerQueue(user);

        return true;
    }

    private void produceNotificationMessageToCompanyAnswerQueue(AppUser user) {
        var telegramUserId = user.getTelegramUserId();
        var answer = telegramView.emailConfirmationNotificationView(telegramUserId);

        user.getCompany().setState(BASIC_STATE);
        user.getCompany().setCurrentView(CompanyView.EMAIL_CONFIRMATION_NOTIFICATION_VIEW);
        appUserRepo.save(user);

        answerProducer.produceAnswer(answer);
    }
}
