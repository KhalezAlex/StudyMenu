package org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.enums.states.DepartmentState;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.inject.Inject;

import static org.klozevitz.enitites.appUsers.enums.views.DepartmentView.*;

@Log4j
@RequiredArgsConstructor
public class BasicStateCQUP implements UpdateProcessor {
    private final DepartmentTelegramView telegramView;
    @Inject
    private AppUserRepo appUserRepo;


    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        var command = update.getCallbackQuery().getData();

        switch (command) {
            case "/start":
                return welcomeView(update, currentAppUser);
            case "/employee_management":
                return employeeManagementView(update, currentAppUser);
            case "/add_employee":
                return employeeTelegramIdRequestView(update, currentAppUser);
            case "/material_management":
                return materialManagementView(update, currentAppUser);
        }
        return null;
    }

    private SendMessage employeeTelegramIdRequestView(Update update, AppUser currentAppUser) {
        currentAppUser.getDepartment().setState(DepartmentState.WAIT_FOR_EMPLOYEE_TG_ID_STATE);
        currentAppUser.getDepartment().setCurrentView(EMPLOYEE_TG_ID_REQUEST_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.employeeTgIdRequestView(update);
    }

    private SendMessage materialManagementView(Update update, AppUser currentAppUser) {
        return null;
    }

    private SendMessage employeeManagementView(Update update, AppUser currentAppUser) {
        currentAppUser.getDepartment().setCurrentView(EMPLOYEES_MANAGEMENT_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.employeesManagementView(update, currentAppUser);
    }

    private SendMessage welcomeView(Update update, AppUser currentAppUser) {
        currentAppUser.getDepartment().setState(DepartmentState.BASIC_STATE);
        currentAppUser.getDepartment().setCurrentView(WELCOME_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.welcomeView(update);
    }
}
