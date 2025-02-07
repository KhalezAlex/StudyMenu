package org.klozevitz.configs;

import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.MessageUtil;
import org.klozevitz.UpdateProcessor;
import org.klozevitz.WrongAppUserDataUpdateProcessor;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.services.implementations.updateProcessors.NotRegisteredAppUserUpdateProcessor;
import org.klozevitz.services.implementations.updateProcessors.NullableStateUpdateProcessor;
import org.klozevitz.services.implementations.updateProcessors.WrongAppUserRoleUpdateProcessor;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.CallbackQueryUpdateProcessor;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState.BasicStateCQUP;
import org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.CommandUpdateProcessor;
import org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.byState.BasicStateCUP;
import org.klozevitz.services.implementations.updateProcessors.textUpdateProcessors.TextUpdateProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

@Configuration
public class UpdateProcessorsConfig {
    @Bean
    public MessageUtil messageUtil() {
        return new MessageUtil();
    }

    @Bean(name = "departmentTelegramView")
    public DepartmentTelegramView telegramView() {
        var messageUtil = messageUtil();
        return new DepartmentTelegramView(messageUtil);
    }

    @Bean(name = "wrongAppUserRoleUpdateProcessor")
    public WrongAppUserDataUpdateProcessor wrongAppUserRoleUpdateProcessor() {
        var telegramView = telegramView();
        return new WrongAppUserRoleUpdateProcessor(telegramView);
    }

    @Bean(name = "notRegisteredAppUserUpdateProcessor")
    public WrongAppUserDataUpdateProcessor notRegisteredAppUserUpdateProcessor() {
        var telegramView = telegramView();
        return new NotRegisteredAppUserUpdateProcessor(telegramView);
    }

    @Bean(name = "nullableStateUpdateProcessor")
    public UpdateProcessor nullableStateUpdateProcessor() {
        var telegramView = telegramView();
        return new NullableStateUpdateProcessor(telegramView);
    }

    /**
     * CommandUpdate-обработчики
     * */
    @Bean(name = "commandUpdateProcessor")
    public UpdateProcessor commandUpdateProcessor() {
        return new CommandUpdateProcessor();
    }

    @Bean(name ="basicStateCommandUpdateProcessor")
    public UpdateProcessor basicStateCommandUpdateProcessor() {
        var telegramView = telegramView();
        return new BasicStateCUP(telegramView);
    }

    /**
     * CallbackQuery-обработчики
     * */
    @Bean("callbackQueryUpdateProcessor")
    public UpdateProcessor callbackQueryUpdateProcessor() {
        return new CallbackQueryUpdateProcessor();
    }

    @Bean("basicStateCallbackQueryUpdateProcessor")
    public UpdateProcessor basicStateCallbackQueryUpdateProcessor() {
        var telegramView = telegramView();
        return new BasicStateCQUP(telegramView);
    }

    /**
     * TextUpdate-обработчики
     * */
    @Bean(name = "textUpdateProcessor")
    public UpdateProcessor textUpdateProcessorService() {
        return new TextUpdateProcessor();
    }
}
