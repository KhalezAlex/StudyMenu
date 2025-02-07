package org.klozevitz.services.implementations.updateProcessors.textUpdateProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.CompanyTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.messageProcessors.TextUpdateProcessor;
import org.klozevitz.messageProcessors.utils.NullableStateUpdateProcessor;
import org.klozevitz.services.interfaces.utils.CompanyActivator;
import org.klozevitz.services.interfaces.utils.DepartmentActivator;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Log4j
@Service
@RequiredArgsConstructor
public class TextUpdateProcessorService implements TextUpdateProcessor {
    private final CompanyTelegramView telegramView;
    private final CompanyActivator companyActivator;
    private final DepartmentActivator departmentActivator;
    private final NullableStateUpdateProcessor nullableStateUpdateProcessor;

    /**
     * Пока предусмотрена передача текстовых сообщений только в 2 статусах
     * В остальных случаях, возвращается только предыдущая страница
     *
     * Когда будем давать возможность компании менять ее название,
     * заведется еще одна ветка в свитче для статуса WAITING_FOR_NAME_STATE
     * */

    @Override
    public SendMessage processTextUpdate(Update update, AppUser currentAppUser) {
        var state = currentAppUser.getCompany().getState();

        if (state == null) {
            return nullableStateUpdateProcessor.processUpdate(update, currentAppUser);
        }

        switch (state) {
            case WAITING_FOR_EMAIL_STATE:
                return companyActivator.setEmail(update, currentAppUser);
            case WAITING_FOR_DEPARTMENT_TELEGRAM_USER_ID_STATE:
                return departmentActivator.registerDepartment(update, currentAppUser);
            default:
                return telegramView.previousView(update, currentAppUser);
        }
    }


}