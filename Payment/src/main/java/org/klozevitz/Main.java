package org.klozevitz;

import org.klozevitz.telegram_bot.TelegramBotApplication;

public class Main {

    private final static String BOT_TOKEN = "";

    private final static String PROVIDER_TOKEN = "";

    public static void main(String[] args) {
        TelegramBotApplication application = TelegramBotApplication.builder()
                .botToken(BOT_TOKEN)
                .providerToken(PROVIDER_TOKEN)
                .build();
        application.run();
    }
}
