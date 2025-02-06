package org.klozevitz.services.implementations;

import lombok.extern.log4j.Log4j;
import org.klozevitz.services.interfaces.Main;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@Service
public class MainService  implements Main {
    @Override
    public void processTextMessage(Update update) {

    }

    @Override
    public void processCommandMessage(Update update) {

    }

    @Override
    public void processCallbackQueryMessage(Update update) {

    }

    @Override
    public void processDocMessage(Update update) {

    }
}
