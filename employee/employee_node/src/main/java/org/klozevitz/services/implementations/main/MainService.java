package org.klozevitz.services.implementations.main;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.klozevitz.services.interfaces.main.Main;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j
@RequiredArgsConstructor
public class MainService implements Main {



    @Override
    public void processTextUpdate(Update update) {

    }

    @Override
    public void processCommandUpdate(Update update) {

    }

    @Override
    public void processCallbackQueryUpdate(Update update) {

    }
}
