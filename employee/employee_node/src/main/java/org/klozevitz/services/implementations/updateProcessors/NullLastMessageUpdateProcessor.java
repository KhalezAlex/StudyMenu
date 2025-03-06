package org.klozevitz.services.implementations.updateProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@RequiredArgsConstructor
public class NullLastMessageUpdateProcessor implements UpdateProcessor<Update> {
    @Override
    public SendMessage processUpdate(Update update) {

        return null;
    }
}
