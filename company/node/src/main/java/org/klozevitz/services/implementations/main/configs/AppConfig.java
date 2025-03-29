package org.klozevitz.services.implementations.main.configs;

import lombok.RequiredArgsConstructor;
import org.klozevitz.MessageUtil;
import org.klozevitz.CompanyTelegramView;
import org.klozevitz.logger.LoggerInfo;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.implementations.main.MainService;
import org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.byState.ContinuousRegistrationCompanyCUP;
import org.klozevitz.services.implementations.updateProcessors.util.NullableStateCompanyUP;
import org.klozevitz.services.implementations.updateProcessors.util.PreviousViewCompanyUP;
import org.klozevitz.services.implementations.updateProcessors.util.WrongAppUserRoleCompanyUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.CallbackQueryCompanyUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState.BasicStateCompanyCQUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState.UnregisteredStateCompanyCQUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState.WaitForDepartmentTgIdStateCompanyCQUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState.WaitingForEmailStateCompanyCQUP;
import org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.CommandCompanyUP;
import org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.byState.BasicStateCompanyCUP;
import org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.byState.UnregisteredStateCompanyCUP;
import org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.byState.WaitForDepartmentTgIdStateCompanyCUP;
import org.klozevitz.services.implementations.updateProcessors.textUpdateProcessors.TextCompanyUpdateProcessorLEGACY;
import org.klozevitz.services.implementations.utils.CompanyRegistrarLEGACY;
import org.klozevitz.services.implementations.utils.DepartmentRegistrarLEGACY;
import org.klozevitz.services.main.AnswerProducer;
import org.klozevitz.services.interfaces.main.Main;
import org.klozevitz.services.messageProcessors.UpdateProcessor_LEGACY;
import org.klozevitz.services.messageProcessors.WrongAppUserDataUpdateProcessor;
import org.klozevitz.services.util.Registrar_LEGACY;
import org.klozevitz.utils.CryptoTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    @Value("${salt}")
    private String salt;
    private final AppUserRepo appUserRepo;
    private final AnswerProducer answerProducer;
    private final LoggerInfo loggerInfo;

    @Bean
    public Main main() {
        return new MainService(
                appUserRepo,
                answerProducer,
                wrongAppUserRoleUpdateProcessor(),
                textUpdateProcessor(),
                commandUpdateProcessor(),
                callbackQueryUpdateProcessor(),
                loggerInfo
        );
    }

    /**
     * Update-обработчики
     */

    @Bean
    public UpdateProcessor_LEGACY nullableStateUpdateProcessor() {
        return new NullableStateCompanyUP(
                appUserRepo,
                telegramView(),
                loggerInfo
        );
    }

    @Bean
    public WrongAppUserDataUpdateProcessor wrongAppUserRoleUpdateProcessor() {
        return new WrongAppUserRoleCompanyUP(
            telegramView(),
            loggerInfo
        );
    }

    @Bean
    public UpdateProcessor_LEGACY previousViewUpdateProcessor() {
        return new PreviousViewCompanyUP(
                telegramView()
        );
    }

    /**
     * TextUpdate-обработчики
     * */
    @Bean
    public UpdateProcessor_LEGACY textUpdateProcessor() {
        return new TextCompanyUpdateProcessorLEGACY(
                companyRegistrar(),
                departmentRegistrar(),
                nullableStateUpdateProcessor(),
                previousViewUpdateProcessor()
        );
    }

    /**
     * CallbackQueryUpdate-обработчики
     */

    @Bean
    public UpdateProcessor_LEGACY callbackQueryUpdateProcessor() {
        return new CallbackQueryCompanyUP(
                nullableStateUpdateProcessor(),
                unregisteredStateCallbackQueryUpdateProcessor(),
                waitingForEmailStateCallbackQueryUpdateProcessor(),
                basicStateCallbackQueryUpdateProcessor(),
                waitingForDepartmentTgIdStateCallbackQueryUpdateProcessor(),
                previousViewUpdateProcessor()
                );
    }

    @Bean
    public UpdateProcessor_LEGACY basicStateCallbackQueryUpdateProcessor() {
        return new BasicStateCompanyCQUP(
                appUserRepo,
                telegramView(),
                previousViewUpdateProcessor()
        );
    }

    @Bean
    UpdateProcessor_LEGACY unregisteredStateCallbackQueryUpdateProcessor() {
        return new UnregisteredStateCompanyCQUP(
                appUserRepo,
                telegramView(),
                previousViewUpdateProcessor()
        );
    }

    @Bean
    public UpdateProcessor_LEGACY waitingForEmailStateCallbackQueryUpdateProcessor() {
        return new WaitingForEmailStateCompanyCQUP(
                appUserRepo,
                telegramView(),
                messageUtil()
        );
    }

    @Bean
    public UpdateProcessor_LEGACY waitingForDepartmentTgIdStateCallbackQueryUpdateProcessor() {
        return new WaitForDepartmentTgIdStateCompanyCQUP(
                appUserRepo,
                telegramView()
        );
    }

    /**
     * CommandUpdate-обработчики
     * */
    @Bean
    public UpdateProcessor_LEGACY commandUpdateProcessor(){
        return new CommandCompanyUP(
                nullableStateUpdateProcessor(),
                basicStateCommandUpdateProcessor(),
                unregisteredStateCommandUpdateProcessor(),
                waitForDepartmentTgIdStateCommandUpdateProcessor(),
                continuousRegistrationCommandUpdateProcessor(),
                previousViewUpdateProcessor()
        );
    }

    @Bean
    public UpdateProcessor_LEGACY basicStateCommandUpdateProcessor() {
        return new BasicStateCompanyCUP(
                appUserRepo,
                telegramView()
        );
    }

    @Bean
    public UpdateProcessor_LEGACY unregisteredStateCommandUpdateProcessor() {
        return new UnregisteredStateCompanyCUP(
                appUserRepo,
                telegramView(),
                previousViewUpdateProcessor()
        );
    }

    @Bean
    public UpdateProcessor_LEGACY waitForDepartmentTgIdStateCommandUpdateProcessor() {
        return new WaitForDepartmentTgIdStateCompanyCUP(
                appUserRepo,
                telegramView(),
                previousViewUpdateProcessor()
        );
    }

    @Bean
    public UpdateProcessor_LEGACY continuousRegistrationCommandUpdateProcessor() {
        return new ContinuousRegistrationCompanyCUP(
                appUserRepo,
                telegramView(),
                previousViewUpdateProcessor()
        );
    }

    /**
     * UTILS
     */
    @Bean
    public CompanyTelegramView telegramView() {
        return new CompanyTelegramView(messageUtil());
    }

    @Bean
    public MessageUtil messageUtil() {
        return new MessageUtil();
    }

    @Bean
    public CryptoTool cryptoTool() {
        return new CryptoTool(salt);
    }

    @Bean
    public Registrar_LEGACY companyRegistrar() {
        return new CompanyRegistrarLEGACY(
                appUserRepo,
                cryptoTool(),
                telegramView(),
                loggerInfo
        );
    }

    @Bean
    public Registrar_LEGACY departmentRegistrar() {
        return new DepartmentRegistrarLEGACY(
                appUserRepo,
                telegramView(),
                loggerInfo
        );
    }
}
