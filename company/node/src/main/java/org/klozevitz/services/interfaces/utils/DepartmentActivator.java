package org.klozevitz.services.interfaces.utils;

import org.klozevitz.enitites.appUsers.AppUser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface DepartmentActivator {
    SendMessage registerDepartment(Update update, AppUser currentAppUser);
}
