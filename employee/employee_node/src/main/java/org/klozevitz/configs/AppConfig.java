package org.klozevitz.configs;

import lombok.RequiredArgsConstructor;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.implementations.main.MainService;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.CallbackQueryEmployeeUP;
import org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.CommandEmployeeUP;
import org.klozevitz.services.implementations.updateProcessors.textUpdateProcessors.TextEmployeeUP;
import org.klozevitz.services.implementations.updateProcessors.util.NotRegisteredAppUserEmployeeUP;
import org.klozevitz.services.implementations.updateProcessors.util.WrongAppUserRoleEmployeeUP;
import org.klozevitz.services.interfaces.main.Main;
import org.klozevitz.services.main.AnswerProducer;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.klozevitz.services.messageProcessors.WrongAppUserDataUpdateProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final AppUserRepo appUserRepo;
    private final AnswerProducer answerProducer;

    @Bean
    public Main main() {
        return new MainService(
                appUserRepo,
                answerProducer,
                wrongAppUserRoleUpdateProcessor(),
                notRegisteredAppUserUpdateProcessor(),
                commandUpdateProcessor(),
                callbackQueryUpdateProcessor(),
                textUpdateProcessor()
        );
    }

    /**
     * Update-обработчики
     * */
    @Bean
    public WrongAppUserDataUpdateProcessor wrongAppUserRoleUpdateProcessor() {
        return new WrongAppUserRoleEmployeeUP();
    }

    @Bean
    public WrongAppUserDataUpdateProcessor notRegisteredAppUserUpdateProcessor() {
        return new NotRegisteredAppUserEmployeeUP();
    }

    /**
     * CommandUpdate-обработчики
     * */
    @Bean
    public UpdateProcessor commandUpdateProcessor() {
        return new CommandEmployeeUP();
    }

    /**
     * CallbackQueryUpdate-обработчики
     * */
    @Bean
    public UpdateProcessor callbackQueryUpdateProcessor() {
        return new CallbackQueryEmployeeUP();
    }

    /**
     * TextUpdate-обработчики
     * */
    @Bean
    public UpdateProcessor textUpdateProcessor() {
        return new TextEmployeeUP();
    }
}
