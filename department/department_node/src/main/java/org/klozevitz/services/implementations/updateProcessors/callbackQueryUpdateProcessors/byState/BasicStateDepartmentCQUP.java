package org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.services.messageProcessors.UpdateProcessor_LEGACY;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.enums.states.DepartmentState;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.klozevitz.enitites.appUsers.enums.states.DepartmentState.WAIT_FOR_DOCUMENT_STATE;
import static org.klozevitz.enitites.appUsers.enums.views.DepartmentView.*;

@Log4j
@RequiredArgsConstructor
public class BasicStateDepartmentCQUP implements UpdateProcessor_LEGACY {
    private final AppUserRepo appUserRepo;
    private final DepartmentTelegramView telegramView;
    private final UpdateProcessor_LEGACY previousViewUpdateProcessor;


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
            case "/resources_management":
                return resourcesManagementView(update, currentAppUser);
            case "/add_resource":
                return addResourceRequestView(update, currentAppUser);
            default:
                return previousViewUpdateProcessor.processUpdate(update, currentAppUser);
        }
    }

    private SendMessage addResourceRequestView(Update update, AppUser currentAppUser) {
        currentAppUser.getDepartment().setCurrentView(RESOURCE_REQUEST_VIEW);
        currentAppUser.getDepartment().setState(WAIT_FOR_DOCUMENT_STATE);
        appUserRepo.save(currentAppUser);

        return telegramView.resourceRequestView(update);
    }

    private SendMessage employeeTelegramIdRequestView(Update update, AppUser currentAppUser) {
        currentAppUser.getDepartment().setState(DepartmentState.WAIT_FOR_EMPLOYEE_TG_ID_STATE);
        currentAppUser.getDepartment().setCurrentView(EMPLOYEE_TG_ID_REQUEST_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.employeeTgIdRequestView(update);
    }

    private SendMessage resourcesManagementView(Update update, AppUser currentAppUser) {
        currentAppUser.getDepartment().setCurrentView(RESOURCES_MANAGEMENT_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.resourcesManagementView(update, currentAppUser);
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
