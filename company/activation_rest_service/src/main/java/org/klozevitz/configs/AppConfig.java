package org.klozevitz.configs;

import org.klozevitz.MessageUtil;
import org.klozevitz.CompanyTelegramView;
import org.klozevitz.utils.CryptoTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${salt}")
    private String salt;

    @Bean
    public CryptoTool cryptoTool() {
        return new CryptoTool(salt);
    }

    @Bean
    public CompanyTelegramView view() {
        return new CompanyTelegramView(messageUtil());
    }

    @Bean
    public MessageUtil messageUtil() {
        return new MessageUtil();
    }
}
