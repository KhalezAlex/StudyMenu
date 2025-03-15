package org.klozevitz.services.interfaces.updateProcessors;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

@FunctionalInterface
public interface UpdateProcessor {
    ArrayList<SendMessage> processUpdate(Update update);
}
