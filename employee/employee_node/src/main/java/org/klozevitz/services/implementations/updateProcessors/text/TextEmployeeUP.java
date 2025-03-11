package org.klozevitz.services.implementations.updateProcessors.text;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;

@Log4j
@RequiredArgsConstructor
public class TextEmployeeUP implements UpdateProcessor {
    private final UpdateProcessor previousViewUpdateProcessor;

    @Override
    public ArrayList<SendMessage> processUpdate(Update update) {
        log.error("В ДАННЫЙ МОМЕНТ, ТЕКСТОВЫЕ СООБЩЕНИЯ НЕ ОБРАБАТЫВАЮТСЯ");
        return previousViewUpdateProcessor.processUpdate(update);
    }
}
