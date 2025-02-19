package org.klozevitz.configs;

import lombok.RequiredArgsConstructor;
import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.MessageUtil;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.implementations.main.MainService;
import org.klozevitz.services.implementations.updateProcessors.docUpdateProcessors.DocumentDepartmentUP;
import org.klozevitz.services.implementations.updateProcessors.textUpdateProcessors.byState.WaitForEmployeeTgIdStateDepartmentTUP;
import org.klozevitz.services.implementations.updateProcessors.util.PreviousViewDepartmentUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState.WaitForDocumentStateDepartmentCQUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState.WaitForEmployeeTgIdStateDepartmentCQUP;
import org.klozevitz.services.implementations.util.EmployeeRegistrar;
import org.klozevitz.services.implementations.util.ExcelToTestParser;
import org.klozevitz.services.interfaces.main.Main;
import org.klozevitz.services.main.AnswerProducer;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.klozevitz.services.messageProcessors.WrongAppUserDataUpdateProcessor;
import org.klozevitz.services.implementations.updateProcessors.util.NotRegisteredAppUserDepartmentUP;
import org.klozevitz.services.implementations.updateProcessors.util.NullableStateDepartmentUP;
import org.klozevitz.services.implementations.updateProcessors.util.WrongAppUserRoleDepartmentUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.CallbackQueryDepartmentUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState.BasicStateDepartmentCQUP;
import org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.CommandDepartmentUP;
import org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.byState.BasicStateDepartmentCUP;
import org.klozevitz.services.implementations.updateProcessors.textUpdateProcessors.TextDepartmentUP;
import org.klozevitz.services.util.Registrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UpdateProcessorsConfig {
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
                textUpdateProcessor(),
                callbackQueryUpdateProcessor(),
                documentUpdateProcessor()
        );
    }

    /**
     * Update-обработчики
     */
    @Bean
    public WrongAppUserDataUpdateProcessor wrongAppUserRoleUpdateProcessor() {
        return new WrongAppUserRoleDepartmentUP(
                telegramView()
        );
    }

    @Bean
    public WrongAppUserDataUpdateProcessor notRegisteredAppUserUpdateProcessor() {
        return new NotRegisteredAppUserDepartmentUP(
                telegramView()
        );
    }

    @Bean
    public UpdateProcessor nullableStateUpdateProcessor() {
        return new NullableStateDepartmentUP(
                appUserRepo,
                telegramView()
        );
    }

    @Bean
    public UpdateProcessor previousViewUpdateProcessor() {
        return new PreviousViewDepartmentUP(
                telegramView()
        );
    }

    /**
     * CommandUpdate-обработчики
     */
    @Bean
    public UpdateProcessor commandUpdateProcessor() {
        return new CommandDepartmentUP(
                nullableStateUpdateProcessor(),
                previousViewUpdateProcessor(),
                basicStateCommandUpdateProcessor()
        );
    }

    @Bean
    public UpdateProcessor basicStateCommandUpdateProcessor() {
        return new BasicStateDepartmentCUP(
                appUserRepo,
                telegramView(),
                previousViewUpdateProcessor()
        );
    }

    /**
     * CallbackQuery-обработчики
     */
    @Bean
    public UpdateProcessor callbackQueryUpdateProcessor() {
        return new CallbackQueryDepartmentUP(
                nullableStateUpdateProcessor(),
                previousViewUpdateProcessor(),
                basicStateCallbackQueryUpdateProcessor(),
                waitForDocumentStateCallbackQueryUpdateProcessor(),
                waitForEmployeeTgIdStateCallbackQueryUpdateProcessor()
        );
    }

    @Bean
    public UpdateProcessor basicStateCallbackQueryUpdateProcessor() {
        return new BasicStateDepartmentCQUP(
                appUserRepo,
                telegramView(),
                previousViewUpdateProcessor()
        );
    }

    @Bean
    public UpdateProcessor waitForDocumentStateCallbackQueryUpdateProcessor() {
        return new WaitForDocumentStateDepartmentCQUP(
                appUserRepo,
                telegramView()
        );
    }

    @Bean
    public UpdateProcessor waitForEmployeeTgIdStateCallbackQueryUpdateProcessor() {
        return new WaitForEmployeeTgIdStateDepartmentCQUP(
                appUserRepo,
                telegramView()
        );
    }

    /**
     * TextUpdate-обработчики
     */
    @Bean
    public UpdateProcessor textUpdateProcessor() {
        return new TextDepartmentUP(
                nullableStateUpdateProcessor(),
                previousViewUpdateProcessor(),
                waitForEmployeeTgIdStateTextUpdateProcessor()
        );
    }

    @Bean
    public UpdateProcessor waitForEmployeeTgIdStateTextUpdateProcessor() {
        return new WaitForEmployeeTgIdStateDepartmentTUP(
                employeeRegistrar()
        );
    }

    /**
     * DocumentUpdate-обработчики
     */
    @Bean
    public UpdateProcessor documentUpdateProcessor() {
        return new DocumentDepartmentUP(
                appUserRepo,
                nullableStateUpdateProcessor(),
                previousViewUpdateProcessor(),
                excelToTestParser());
    }


    /**
     * UTIL-бины
     */
    @Bean
    public MessageUtil messageUtil() {
        return new MessageUtil();
    }

    @Bean
    public DepartmentTelegramView telegramView() {
        return new DepartmentTelegramView(
                messageUtil()
        );
    }
    @Bean
    public ExcelToTestParser excelToTestParser() {
        return new ExcelToTestParser();
    }

    @Bean
    public Registrar employeeRegistrar() {
        return new EmployeeRegistrar(
                telegramView()
        );
    }
}
