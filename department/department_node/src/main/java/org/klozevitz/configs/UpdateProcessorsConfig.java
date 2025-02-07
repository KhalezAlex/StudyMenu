package org.klozevitz.configs;

import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.MessageUtil;
import org.klozevitz.UpdateProcessor;
import org.klozevitz.WrongAppUserDataUpdateProcessor;
import org.klozevitz.services.implementations.updateProcessors.NotRegisteredAppUserUpdateProcessor;
import org.klozevitz.services.implementations.updateProcessors.NullableStateUpdateProcessor;
import org.klozevitz.services.implementations.updateProcessors.WrongAppUserRoleUpdateProcessor;
import org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.CommandUpdateProcessor;
import org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.byState.BasicStateCUP;
import org.klozevitz.services.implementations.updateProcessors.textUpdateProcessors.TextUpdateProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpdateProcessorsConfig {
    @Bean
    public MessageUtil messageUtil() {
        return new MessageUtil();
    }

    @Bean
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
     * TextUpdate-обработчики
     * */
    @Bean(name = "textUpdateProcessor")
    public UpdateProcessor textUpdateProcessorService() {
        return new TextUpdateProcessor();
    }
}
