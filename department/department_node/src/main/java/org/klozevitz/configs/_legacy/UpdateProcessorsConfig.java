package org.klozevitz.configs._legacy;

import lombok.RequiredArgsConstructor;
import org.klozevitz.DepartmentTelegramView;
import org.klozevitz.MessageUtil;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.implementations.util.ExcelToTestParser;
import org.klozevitz.services.interfaces.main.Main;
import org.klozevitz.services.main.AnswerProducer;
import org.klozevitz.services.messageProcessors.UpdateProcessor_LEGACY;
import org.klozevitz.services.messageProcessors.WrongAppUserDataUpdateProcessor;
import org.klozevitz.services.util.Registrar_LEGACY;

@RequiredArgsConstructor
public class UpdateProcessorsConfig {
    private final AppUserRepo appUserRepo;
    private final AnswerProducer answerProducer;

//    @Bean
    public Main main() {
//        return new MainService(
//                appUserRepo,
//                answerProducer,
//                wrongAppUserRoleUpdateProcessor(),
//                notRegisteredAppUserUpdateProcessor(),
//                commandUpdateProcessor(),
//                textUpdateProcessor(),
//                callbackQueryUpdateProcessor(),
//                documentUpdateProcessor()
//        );
        return null;
    }

    /**
     * Update-обработчики
     */
//    @Bean
    public WrongAppUserDataUpdateProcessor wrongAppUserRoleUpdateProcessor() {
//        return new WrongAppUserRoleDepartmentUP(
//                telegramView()
//        );
        return null;
    }

//    @Bean
    public WrongAppUserDataUpdateProcessor notRegisteredAppUserUpdateProcessor() {
//        return new NotRegisteredAppUserDepartmentUP(
//                telegramView()
//        );
        return null;
    }

//    @Bean
    public UpdateProcessor_LEGACY nullableStateUpdateProcessor() {
//        return new NullableStateDepartmentUP(
//                appUserRepo,
//                telegramView()
//        );
        return null;
    }

//    @Bean
    public UpdateProcessor_LEGACY previousViewUpdateProcessor() {
//        return new PreviousViewDepartmentUP(
//                telegramView()
//        );
        return null;
    }

    /**
     * CommandUpdate-обработчики
     */
//    @Bean
    public UpdateProcessor_LEGACY commandUpdateProcessor() {
//        return new CommandDepartmentUP_LEGACY(
//                nullableStateUpdateProcessor(),
//                previousViewUpdateProcessor(),
//                basicStateCommandUpdateProcessor()
//        );
        return null;
    }

//    @Bean
    public UpdateProcessor_LEGACY basicStateCommandUpdateProcessor() {
//        return new BasicStateDepartmentCUP(
//                appUserRepo,
//                telegramView(),
//                previousViewUpdateProcessor()
//        );
        return null;
    }

    /**
     * CallbackQuery-обработчики
     */
//    @Bean
    public UpdateProcessor_LEGACY callbackQueryUpdateProcessor() {
//        return new CallbackQueryDepartmentUP(
//                nullableStateUpdateProcessor(),
//                previousViewUpdateProcessor(),
//                basicStateCallbackQueryUpdateProcessor(),
//                waitForDocumentStateCallbackQueryUpdateProcessor(),
//                waitForEmployeeTgIdStateCallbackQueryUpdateProcessor()
//        );
        return null;
    }

//    @Bean
    public UpdateProcessor_LEGACY basicStateCallbackQueryUpdateProcessor() {
//        return new BasicStateDepartmentCQUP(
//                appUserRepo,
//                telegramView(),
//                previousViewUpdateProcessor()
//        );
        return null;
    }

//    @Bean
    public UpdateProcessor_LEGACY waitForDocumentStateCallbackQueryUpdateProcessor() {
//        return new WaitForDocumentStateDepartmentCQUP(
//                appUserRepo,
//                telegramView()
//        );
        return null;
    }

//    @Bean
    public UpdateProcessor_LEGACY waitForEmployeeTgIdStateCallbackQueryUpdateProcessor() {
//        return new WaitForEmployeeTgIdStateDepartmentCQUP(
//                appUserRepo,
//                telegramView()
//        );
        return null;
    }

    /**
     * TextUpdate-обработчики
     */
//    @Bean
    public UpdateProcessor_LEGACY textUpdateProcessor() {
//        return new TextDepartmentUP(
//                nullableStateUpdateProcessor(),
//                previousViewUpdateProcessor(),
//                waitForEmployeeTgIdStateTextUpdateProcessor()
//        );
        return null;
    }

//    @Bean
    public UpdateProcessor_LEGACY waitForEmployeeTgIdStateTextUpdateProcessor() {
//        return new WaitForEmployeeTgIdStateDepartmentTUP(
//                employeeRegistrar()
//        );
        return null;
    }

    /**
     * DocumentUpdate-обработчики
     */
//    @Bean
    public UpdateProcessor_LEGACY documentUpdateProcessor() {
//        return new DocumentDepartmentUP(
//                appUserRepo,
//                nullableStateUpdateProcessor(),
//                previousViewUpdateProcessor(),
//                excelToTestParser());
        return null;
    }


    /**
     * UTIL-бины
     */
//    @Bean
    public MessageUtil messageUtil() {
        return new MessageUtil();
    }

//    @Bean
    public DepartmentTelegramView telegramView() {
//        return new DepartmentTelegramView(
//                messageUtil()
//        );
        return null;
    }
//    @Bean
    public ExcelToTestParser excelToTestParser() {
        return new ExcelToTestParser();
    }

//    @Bean
    public Registrar_LEGACY employeeRegistrar() {
//        return new EmployeeRegistrar(
//                telegramView()
//        );
        return null;
    }
}
