package org.klozevitz.configs;

import lombok.RequiredArgsConstructor;
import org.klozevitz.MessageUtil;
import org.klozevitz.CompanyTelegramView;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.implementations.main.MainService;
import org.klozevitz.services.implementations.updateProcessors.util.NullableStateCompanyUP;
import org.klozevitz.services.implementations.updateProcessors.util.PreviousViewCompanyUpdateProcessor;
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
import org.klozevitz.services.implementations.updateProcessors.textUpdateProcessors.TextCompanyUpdateProcessor;
import org.klozevitz.services.implementations.utils.CompanyRegistrar;
import org.klozevitz.services.implementations.utils.DepartmentRegistrar;
import org.klozevitz.services.main.AnswerProducer;
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
        return new NullableStateCompanyUP(
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

    @Bean
    public UpdateProcessor previousViewUpdateProcessor() {
        return new PreviousViewCompanyUpdateProcessor(
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
    public UpdateProcessor basicStateCallbackQueryUpdateProcessor() {
        return new BasicStateCompanyCQUP(
                appUserRepo,
                telegramView(),
                previousViewUpdateProcessor()
        );
    }

    @Bean
    UpdateProcessor unregisteredStateCallbackQueryUpdateProcessor() {
        return new UnregisteredStateCompanyCQUP(
                appUserRepo,
                telegramView(),
                previousViewUpdateProcessor()
        );
    }

    @Bean
    public UpdateProcessor waitingForEmailStateCallbackQueryUpdateProcessor() {
        return new WaitingForEmailStateCompanyCQUP(
                appUserRepo,
                telegramView(),
                messageUtil()
        );
    }

    @Bean
    public UpdateProcessor waitingForDepartmentTgIdStateCallbackQueryUpdateProcessor() {
        return new WaitForDepartmentTgIdStateCompanyCQUP(
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
                waitForDepartmentTgIdStateCommandUpdateProcessor(),
                previousViewUpdateProcessor()
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
