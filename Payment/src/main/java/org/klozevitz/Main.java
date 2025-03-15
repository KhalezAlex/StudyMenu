package org.klozevitz;

import org.klozevitz.telegram_bot.TelegramBotApplication;

public class Main {
    //TODO пока не понял как из файла app подтягивать данные
    private static final String BOT_TOKEN = "";
    private static final String PROVIDER_TOKEN = "";

    public static void main(String[] args) {
        TelegramBotApplication application = TelegramBotApplication.builder()
                .botToken(BOT_TOKEN)
                .providerToken(PROVIDER_TOKEN)
                .build();
        application.run();
    }
}
