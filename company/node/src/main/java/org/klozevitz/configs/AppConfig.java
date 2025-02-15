package org.klozevitz.configs;

import lombok.RequiredArgsConstructor;
import org.klozevitz.MessageUtil;
import org.klozevitz.CompanyTelegramView;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.implementations.main.MainService;
import org.klozevitz.services.implementations.updateProcessors.CompanyNullableStateUP;
import org.klozevitz.services.implementations.updateProcessors.WrongAppUserRoleCompanyUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.CompanyCallbackQueryUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState.CompanyBasicStateCQUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState.CompanyUnregisteredStateCQUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState.CompanyWaitingForDepartmentTgIdStateCQUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState.CompanyWaitingForEmailStateCQUP;
import org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.CommandCompanyUP;
import org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.byState.BasicStateCompanyCUP;
import org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.byState.UnregisteredStateCompanyCUP;
import org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.byState.WaitForDepartmentTgIdStateCompanyCUP;
import org.klozevitz.services.implementations.updateProcessors.textUpdateProcessors.TextCompanyUpdateProcessor;
import org.klozevitz.services.implementations.utils.CompanyRegistrar;
import org.klozevitz.services.implementations.utils.DepartmentRegistrar;
import org.klozevitz.services.interfaces.main.AnswerProducer;
import org.klozevitz.services.interfaces.main.Main;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.klozevitz.services.messageProcessors.WrongAppUserDataUpdateProcessor;
import org.klozevitz.services.util.Registrar;
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
    public Main main() {
        return new MainService(
                appUserRepo,
                answerProducer,
                wrongAppUserRoleUpdateProcessor(),
                textUpdateProcessor(),
                commandUpdateProcessor(),
                callbackQueryUpdateProcessor()
        );
    }

    /**
     * Update-обработчики
     */

    @Bean
    public UpdateProcessor nullableStateUpdateProcessor() {
        return new CompanyNullableStateUP(
                appUserRepo,
                telegramView()
        );
    }

    @Bean
    public WrongAppUserDataUpdateProcessor wrongAppUserRoleUpdateProcessor() {
        return new WrongAppUserRoleCompanyUP(
            telegramView()
        );
    }

    /**
     * Text-update-обработчики
     * */
    @Bean
    public UpdateProcessor textUpdateProcessor() {
        return new TextCompanyUpdateProcessor(
                telegramView(),
                companyRegistrar(),
                departmentRegistrar(),
                nullableStateUpdateProcessor()
        );
    }

    /**
     * CallbackQuery-update-обработчики
     */

    @Bean
    public UpdateProcessor callbackQueryUpdateProcessor() {
        return new CompanyCallbackQueryUP(
                telegramView(),
                nullableStateUpdateProcessor(),
                unregisteredStateCallbackQueryUpdateProcessor(),
                waitingForEmailStateCallbackQueryUpdateProcessor(),
                basicStateCallbackQueryUpdateProcessor(),
                waitingForDepartmentTgIdStateCallbackQueryUpdateProcessor()
                );
    }

    @Bean
    public UpdateProcessor basicStateCallbackQueryUpdateProcessor() {
        return new CompanyBasicStateCQUP(
                appUserRepo,
                telegramView()
        );
    }

    @Bean
    UpdateProcessor unregisteredStateCallbackQueryUpdateProcessor() {
        return new CompanyUnregisteredStateCQUP(
                appUserRepo,
                telegramView()
        );
    }

    @Bean
    public UpdateProcessor waitingForEmailStateCallbackQueryUpdateProcessor() {
        return new CompanyWaitingForEmailStateCQUP(
                appUserRepo,
                telegramView(),
                messageUtil()
        );
    }

    @Bean
    public UpdateProcessor waitingForDepartmentTgIdStateCallbackQueryUpdateProcessor() {
        return new CompanyWaitingForDepartmentTgIdStateCQUP(
                appUserRepo,
                telegramView()
        );
    }

    /**
     * Command-update-обработчики
     * */
    // TODO обязательно первые два параметра положить в один из сервисов, которых еще нет.
    //  Предположительно, в сервис, который будет работать на уровне регистрации компании
    @Bean
    public UpdateProcessor commandUpdateProcessor(){
        return new CommandCompanyUP(
                appUserRepo,
                telegramView(),
                nullableStateUpdateProcessor(),
                basicStateCommandUpdateProcessor(),
                unregisteredStateCommandUpdateProcessor(),
                waitForDepartmentTgIdStateCommandUpdateProcessor()
        );
    }

    @Bean
    public UpdateProcessor basicStateCommandUpdateProcessor() {
        return new BasicStateCompanyCUP(
                appUserRepo,
                telegramView()
        );
    }

    @Bean
    public UpdateProcessor unregisteredStateCommandUpdateProcessor() {
        return new UnregisteredStateCompanyCUP(
                appUserRepo,
                telegramView()
        );
    }

    @Bean
    public UpdateProcessor waitForDepartmentTgIdStateCommandUpdateProcessor() {
        return new WaitForDepartmentTgIdStateCompanyCUP(
                appUserRepo,
                telegramView()
        );
    }

    /**
     * UTILS
     */
    @Bean
    public Registrar companyRegistrar() {
        return new CompanyRegistrar(
                appUserRepo,
                cryptoTool(),
                telegramView()
        );
    }

    @Bean
    public Registrar departmentRegistrar() {
        return new DepartmentRegistrar(
                appUserRepo,
                telegramView()
        );
    }
}
