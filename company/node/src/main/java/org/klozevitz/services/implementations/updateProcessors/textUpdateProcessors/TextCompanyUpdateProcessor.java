package org.klozevitz.services.implementations.updateProcessors.textUpdateProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.CompanyTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.klozevitz.services.util.Registrar;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Log4j
@RequiredArgsConstructor
public class TextCompanyUpdateProcessor implements UpdateProcessor {
    private final CompanyTelegramView telegramView;
    private final Registrar companyRegistrar;
    private final Registrar departmentRegistrar;
    private final UpdateProcessor nullableStateUpdateProcessor;

    /**
     * Пока предусмотрена передача текстовых сообщений только в 2 статусах
     * В остальных случаях, возвращается только предыдущая страница
     *
     * Когда будем давать возможность компании менять ее название,
     * заведется еще одна ветка в свитче для статуса WAITING_FOR_NAME_STATE
     * */

    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        var state = currentAppUser.getCompany().getState();

        if (state == null) {
            return nullableStateUpdateProcessor.processUpdate(update, currentAppUser);
        }

        switch (state) {
            case WAIT_FOR_EMAIL_STATE:
                return companyRegistrar.register(update, currentAppUser);
            case WAIT_FOR_DEPARTMENT_TELEGRAM_USER_ID_STATE:
                return departmentRegistrar.register(update, currentAppUser);
            default:
                return telegramView.previousView(update, currentAppUser);
        }
    }


}