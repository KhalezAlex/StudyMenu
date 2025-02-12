package org.klozevitz.configs;

import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.MessageUtil;
import org.klozevitz.services.implementations.updateProcessors.docUpdateProcessors.DocumentUP;
import org.klozevitz.services.implementations.updateProcessors.textUpdateProcessors.byState.WaitForEmployeeTgIdStateTUP;
import org.klozevitz.services.implementations.updateProcessors.util.PreviousViewUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState.WaitForDocumentStateCQUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState.WaitForEmployeeTgIdStateCQUP;
import org.klozevitz.services.implementations.util.EmployeeRegistrar;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.klozevitz.services.messageProcessors.WrongAppUserDataUpdateProcessor;
import org.klozevitz.services.implementations.updateProcessors.util.NotRegisteredAppUserUP;
import org.klozevitz.services.implementations.updateProcessors.util.NullableStateUP;
import org.klozevitz.services.implementations.updateProcessors.util.WrongAppUserRoleUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.CallbackQueryUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState.BasicStateCQUP;
import org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.CommandUP;
import org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.byState.BasicStateCUP;
import org.klozevitz.services.implementations.updateProcessors.textUpdateProcessors.TextUP;
import org.klozevitz.services.uitl.Registrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    @Bean(name = "wrongAppUserRole_UpdateProcessor")
    public WrongAppUserDataUpdateProcessor wrongAppUserRoleUpdateProcessor() {
        var telegramView = telegramView();
        return new WrongAppUserRoleUP(telegramView);
    }

    @Bean(name = "notRegisteredAppUser_UpdateProcessor")
    public WrongAppUserDataUpdateProcessor notRegisteredAppUserUpdateProcessor() {
        var telegramView = telegramView();
        return new NotRegisteredAppUserUP(telegramView);
    }

    @Bean(name = "nullableState_UpdateProcessor")
    public UpdateProcessor nullableStateUpdateProcessor() {
        var telegramView = telegramView();
        return new NullableStateUP(telegramView);
    }

    @Bean(name = "previousView_UpdateProcessor")
    public UpdateProcessor previousViewUpdateProcessor() {
        var telegramView = telegramView();
        return new PreviousViewUP(telegramView);
    }

    /**
     * CommandUpdate-обработчики
     * */
    @Bean(name = "command_UpdateProcessor")
    public UpdateProcessor commandUpdateProcessor() {
        return new CommandUP();
    }

    @Bean(name = "basicStateCommand_UpdateProcessor")
    public UpdateProcessor basicStateCommandUpdateProcessor() {
        var telegramView = telegramView();
        return new BasicStateCUP(telegramView);
    }

    /**
     * CallbackQuery-обработчики
     * */
    @Bean("callbackQuery_UpdateProcessor")
    public UpdateProcessor callbackQueryUpdateProcessor() {
        return new CallbackQueryUP();
    }

    @Bean("basicState_CallbackQuery_UpdateProcessor")
    public UpdateProcessor basicStateCallbackQueryUpdateProcessor() {
        var telegramView = telegramView();
        return new BasicStateCQUP(telegramView);
    }

    @Bean("waitForDocumentState_CallbackQuery_UpdateProcessor")
    public UpdateProcessor waitForDocumentStateCallbackQueryUpdateProcessor() {
        var telegramView = telegramView();
        return new WaitForDocumentStateCQUP(telegramView);
    }

    @Bean("waitForEmployeeTgIdState_CallbackQuery_UpdateProcessor")
    public UpdateProcessor waitForEmployeeTgIdStateCallbackQueryUpdateProcessor() {
        var telegramView = telegramView();
        return new WaitForEmployeeTgIdStateCQUP(telegramView);
    }

    @Bean
    public Registrar employeeRegistrar() {
        var telegramView = telegramView();
        return new EmployeeRegistrar(telegramView);
    }

    /**
     * TextUpdate-обработчики
     * */
    @Bean(name = "text_UpdateProcessor")
    public UpdateProcessor textUpdateProcessorService() {
        return new TextUP(nullableStateUpdateProcessor());
    }

    @Bean(name = "waitForEmployeeTgIdState_Text_UpdateProcessor")
    public UpdateProcessor waitForEmployeeTgIdStateTextUpdateProcessor() {
        var employeeRegistrar = employeeRegistrar();
        return new WaitForEmployeeTgIdStateTUP(employeeRegistrar);
    }

    /**
     * DocumentUpdate-обработчики
     * */
    @Bean(name = "document_UpdateProcessor")
    public UpdateProcessor documentUpdateProcessor() {
        return new DocumentUP();
    }
}
