package org.klozevitz.services.util;

import org.klozevitz.enitites.appUsers.AppUser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.regex.Pattern;

public interface Registrar {
    SendMessage register(Update update, AppUser currentAppUser);

    default boolean isTgIdValid(String tgId) {
        var regexp = "\\b\\d{8,11}\\b";
        var pattern = Pattern.compile(regexp);
        var matcher = pattern.matcher(tgId);

        return matcher.matches();
    }
}
