package org.klozevitz.services.implementations.updateProcessors.textUpdateProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.services.messageProcessors.UpdateProcessor_LEGACY;
import org.klozevitz.services.util.Registrar_LEGACY;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Log4j
@RequiredArgsConstructor
public class TextCompanyUpdateProcessorLEGACY implements UpdateProcessor_LEGACY {
    private final Registrar_LEGACY companyRegistrarLEGACY;
    private final Registrar_LEGACY departmentRegistrarLEGACY;
    private final UpdateProcessor_LEGACY nullableStateUpdateProcessor;
    private final UpdateProcessor_LEGACY previousViewUpdateProcessor;


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
                log.info("WAIT_FOR_EMAIL_STATE: " + update.getMessage().getFrom().getId());
                return companyRegistrarLEGACY.register(update, currentAppUser);
            case WAIT_FOR_DEPARTMENT_TG_ID_STATE:
                log.info("WAIT_FOR_DEPARTMENT_TG_ID_STATE: " + update.getMessage().getFrom().getId());
                return departmentRegistrarLEGACY.register(update, currentAppUser);
            default:
                log.error("Сообщение из TextCompanyUpdateProcessor не попало ни в одну из веток состояний компании" + update);
                return previousViewUpdateProcessor.processUpdate(update, currentAppUser);
        }
    }


}