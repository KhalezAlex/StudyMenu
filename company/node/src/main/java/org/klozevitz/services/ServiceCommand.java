package org.klozevitz.services;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
public enum ServiceCommand {
    START("/start"),
    HELP("/help"),
    CANCEL("/cancel");

    private final String value;

    @Override
    public String toString() {
        return value;
    }

    public boolean equalsUpdateCommand(Update update) {
        return this.toString().equals(update.getMessage().getText());
    }

    public static ServiceCommand serviceCommandFromUpdate(Update update) {
        var serviceCommand = update.getMessage().getText();
        for (ServiceCommand sc: ServiceCommand.values()) {
            if (sc.value.equals(serviceCommand)) {
                return sc;
            }
        }
        return null;
    }
}
