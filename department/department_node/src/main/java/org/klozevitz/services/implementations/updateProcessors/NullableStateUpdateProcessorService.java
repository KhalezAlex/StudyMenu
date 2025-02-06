package org.klozevitz.services.implementations.updateProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.Department;
import org.klozevitz.messageProcessors.NullableStateUpdateProcessor;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.enitites.appUsers.enums.views.DepartmentView.WRONG_APP_USER_ROLE_NOTIFICATION_VIEW;

@Log4j
@Service
@RequiredArgsConstructor
public class NullableStateUpdateProcessorService implements NullableStateUpdateProcessor {
    private final DepartmentTelegramView telegramView;
    private final AppUserRepo appUserRepo;


    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        currentAppUser.getDepartment().setCurrentView(WRONG_APP_USER_ROLE_NOTIFICATION_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.wrongAppUserRoleErrorView(update);
    }
}
