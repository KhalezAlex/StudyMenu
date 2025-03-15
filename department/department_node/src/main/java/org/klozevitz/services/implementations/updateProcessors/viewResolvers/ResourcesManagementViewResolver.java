package org.klozevitz.services.implementations.updateProcessors.viewResolvers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.repositories.appUsers.DepartmentRepo;
import org.klozevitz.services.interfaces.updateProcessors.BasicUpdateProcessor;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

import static org.klozevitz.enitites.appUsers.enums.views.DepartmentView.RESOURCES_MANAGEMENT_VIEW;

@Log4j
@RequiredArgsConstructor
public class ResourcesManagementViewResolver extends BasicUpdateProcessor {
    private final AppUserRepo appUserRepo;
    private final DepartmentRepo departmentRepo;
    private final DepartmentTelegramView telegramView;
    private final UpdateProcessor notRegisteredAppUserUpdateProcessor;
//    private final UpdateProcessor previousViewUpdateProcessor;

    @Override
    public ArrayList<SendMessage> processUpdate(Update update) {
        var telegramUserId = telegramUserId(update);

        departmentRepo.setDepartmentCurrentView(RESOURCES_MANAGEMENT_VIEW.name(), telegramUserId);

        var optionalCurrentAppUser = appUserRepo.findByTelegramUserId(telegramUserId);
        var answer = telegramView.resourcesManagementView(update, optionalCurrentAppUser.get());

        return answerAsList(answer);
    }
}
