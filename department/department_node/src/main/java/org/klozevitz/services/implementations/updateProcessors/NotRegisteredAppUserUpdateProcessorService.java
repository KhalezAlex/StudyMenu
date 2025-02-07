package org.klozevitz.services.implementations.updateProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.services.interfaces.updateProcessors.NotRegisteredAppUserUpdateProcessor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log
@Service
@RequiredArgsConstructor
public class NotRegisteredAppUserUpdateProcessorService implements NotRegisteredAppUserUpdateProcessor {
    private final DepartmentTelegramView telegramView;

    @Override
    public SendMessage processUpdate(Update update) {
        return telegramView.notRegisteredDepartmentErrorView(update);
    }
}
