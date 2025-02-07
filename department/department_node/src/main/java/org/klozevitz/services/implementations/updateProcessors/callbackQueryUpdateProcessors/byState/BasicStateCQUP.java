package org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.UpdateProcessor;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.enums.views.DepartmentView;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.inject.Inject;

import static org.klozevitz.enitites.appUsers.enums.views.DepartmentView.EMPLOYEES_MANAGEMENT_VIEW;

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
            case "/employee_management":
                return employeeManagementView(update, currentAppUser);
            case "/material_management":
                return materialManagementView(update, currentAppUser);
        }
        return null;
    }

    private SendMessage materialManagementView(Update update, AppUser currentAppUser) {
        return null;
    }

    private SendMessage employeeManagementView(Update update, AppUser currentAppUser) {
        currentAppUser.getDepartment().setCurrentView(EMPLOYEES_MANAGEMENT_VIEW);
        appUserRepo.save(currentAppUser);

        return telegramView.employeesManagementView(update, currentAppUser);
    }
}
