package org.klozevitz.configs;

import lombok.RequiredArgsConstructor;
import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.MessageUtil;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.implementations.main.MainService;
import org.klozevitz.services.implementations.updateProcessors.docUpdateProcessors.DocumentUP;
import org.klozevitz.services.implementations.updateProcessors.textUpdateProcessors.byState.WaitForEmployeeTgIdStateTUP;
import org.klozevitz.services.implementations.updateProcessors.util.PreviousViewUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState.WaitForDocumentStateCQUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState.WaitForEmployeeTgIdStateCQUP;
import org.klozevitz.services.implementations.util.EmployeeRegistrar;
import org.klozevitz.services.implementations.util.ExcelToTestParser;
import org.klozevitz.services.interfaces.main.AnswerProducer;
import org.klozevitz.services.interfaces.main.Main;
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

    @Bean
    public WrongAppUserDataUpdateProcessor wrongAppUserRoleUpdateProcessor() {
        return new WrongAppUserRoleUP(
                telegramView()
        );
    }

    @Bean
    public WrongAppUserDataUpdateProcessor notRegisteredAppUserUpdateProcessor() {
        return new NotRegisteredAppUserUP(
                telegramView()
        );
    }

    @Bean
    public UpdateProcessor nullableStateUpdateProcessor() {
        return new NullableStateUP(
                appUserRepo,
                telegramView()
        );
    }

    @Bean
    public UpdateProcessor previousViewUpdateProcessor() {
        return new PreviousViewUP(
                telegramView()
        );
    }

    /**
     * CommandUpdate-обработчики
     */
    @Bean
    public UpdateProcessor commandUpdateProcessor() {
        return new CommandUP(
                nullableStateUpdateProcessor(),
                previousViewUpdateProcessor(),
                basicStateCommandUpdateProcessor()
        );
    }

    @Bean
    public UpdateProcessor basicStateCommandUpdateProcessor() {
        return new BasicStateCUP(
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
        return new CallbackQueryUP(
                nullableStateUpdateProcessor(),
                previousViewUpdateProcessor(),
                basicStateCallbackQueryUpdateProcessor(),
                waitForDocumentStateCallbackQueryUpdateProcessor(),
                waitForEmployeeTgIdStateCallbackQueryUpdateProcessor()
        );
    }

    @Bean
    public UpdateProcessor basicStateCallbackQueryUpdateProcessor() {
        return new BasicStateCQUP(
                appUserRepo,
                telegramView(),
                previousViewUpdateProcessor()
        );
    }

    @Bean
    public UpdateProcessor waitForDocumentStateCallbackQueryUpdateProcessor() {
        return new WaitForDocumentStateCQUP(
                appUserRepo,
                telegramView()
        );
    }

    @Bean
    public UpdateProcessor waitForEmployeeTgIdStateCallbackQueryUpdateProcessor() {
        return new WaitForEmployeeTgIdStateCQUP(
                appUserRepo,
                telegramView()
        );
    }

    /**
     * TextUpdate-обработчики
     */
    @Bean
    public UpdateProcessor textUpdateProcessor() {
        return new TextUP(
                nullableStateUpdateProcessor(),
                previousViewUpdateProcessor(),
                waitForEmployeeTgIdStateTextUpdateProcessor()
        );
    }

    @Bean
    public UpdateProcessor waitForEmployeeTgIdStateTextUpdateProcessor() {
        return new WaitForEmployeeTgIdStateTUP(
                employeeRegistrar()
        );
    }

    /**
     * DocumentUpdate-обработчики
     */
    @Bean
    public UpdateProcessor documentUpdateProcessor() {
        return new DocumentUP(
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
