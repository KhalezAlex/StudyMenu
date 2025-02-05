package org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState;

import lombok.RequiredArgsConstructor;
import org.klozevitz.TelegramView;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.enums.states.CompanyState;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.interfaces.updateProcessors.callbackQueryUpdateProcessors.BasicStateCQUP;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.enitites.appUsers.enums.states.CompanyState.WAITING_FOR_DEPARTMENT_TELEGRAM_USER_ID_STATE;
import static org.klozevitz.enitites.appUsers.enums.views.CompanyView.*;

@Service
@RequiredArgsConstructor
public class BasicStateCQUP_Service implements BasicStateCQUP {
    private final TelegramView telegramView;
    private final AppUserRepo appUserRepo;

    @Override
    public SendMessage processCallbackQueryMessage(Update update, AppUser currentAppUser) {
        var command = update.getCallbackQuery().getData();

        switch (command) {
            case "/start":
                return registeredWelcomeView(update, currentAppUser);
            case "/departments_management":
                return departmentManagementView(update, currentAppUser);
            case "/add_department":
                return departmentTelegramUserIdRequestView(update, currentAppUser);
            default:
                return telegramView.previousView(update, currentAppUser);
        }
    }


    private SendMessage registeredWelcomeView(Update update, AppUser currentAppUser) {
        currentAppUser.getCompany().setCurrentView(REGISTERED_WELCOME_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.registeredWelcomeView(update);
    }
    private SendMessage departmentManagementView(Update update, AppUser currentAppUser) {
        currentAppUser.getCompany().setCurrentView(DEPARTMENTS_MANAGEMENT_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.departmentsManagementView(update, currentAppUser);
    }

    private SendMessage departmentTelegramUserIdRequestView(Update update, AppUser currentAppUser) {
        currentAppUser.getCompany().setState(WAITING_FOR_DEPARTMENT_TELEGRAM_USER_ID_STATE);
        currentAppUser.getCompany().setCurrentView(DEPARTMENT_TELEGRAM_USER_ID_REQUEST_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.departmentTelegramUserIdRequestView(update);
    }
}
