package org.klozevitz.services.implementations.updateProcessors.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.services.messageProcessors.UpdateProcessor_LEGACY;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.enitites.appUsers.enums.views.DepartmentView.NULL_DEPARTMENT_STATE_ERROR_VIEW;

@Log4j
@RequiredArgsConstructor
public class NullableStateDepartmentUP implements UpdateProcessor_LEGACY {
    private final AppUserRepo appUserRepo;
    private final DepartmentTelegramView telegramView;


    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        currentAppUser.getDepartment().setCurrentView(NULL_DEPARTMENT_STATE_ERROR_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.nullDepartmentStateErrorView(update);
    }
}
