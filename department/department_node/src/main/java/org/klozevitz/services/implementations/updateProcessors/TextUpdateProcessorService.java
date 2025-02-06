package org.klozevitz.services.implementations.updateProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.messageProcessors.TextUpdateProcessor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@Service
@RequiredArgsConstructor
public class TextUpdateProcessorService implements TextUpdateProcessor {

    @Override
    public SendMessage processTextUpdate(Update update, AppUser currentAppUser) {
        return null;
    }
}
