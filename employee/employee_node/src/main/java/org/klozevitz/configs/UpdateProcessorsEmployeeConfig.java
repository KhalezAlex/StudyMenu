package org.klozevitz.configs;

import lombok.RequiredArgsConstructor;
import org.klozevitz.EmployeeTelegramView;
import org.klozevitz.MessageUtil;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.enums.views.EmployeeView;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.repositories.appUsers.EmployeeRepo;
import org.klozevitz.services.implementations.main.MainService;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.CallbackQueryEmployeeUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState.BasicStateEmployeeCQUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState.StudyStatePreviousViewUP;
import org.klozevitz.services.implementations.updateProcessors.callbackQueryUpdateProcessors.byState.StudyStateEmployeeCQUP;
import org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.CommandEmployeeUP;
import org.klozevitz.services.implementations.updateProcessors.commandUpdateProcessors.byState.BasicStateEmployeeCUP;
import org.klozevitz.services.implementations.updateProcessors.util.NotRegisteredAppUserEmployeeUP;
import org.klozevitz.services.implementations.updateProcessors.util.NullableStateEmployeeUP;
import org.klozevitz.services.implementations.updateProcessors.util.PreviousViewEmployeeUP;
import org.klozevitz.services.implementations.updateProcessors.util.WrongAppUserRoleEmployeeUP;
import org.klozevitz.services.interfaces.main.AnswerProducer;
import org.klozevitz.services.interfaces.main.Main;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class UpdateProcessorsEmployeeConfig {
    private final ApplicationContext appContext;
    private final AppUserRepo appUserRepo;
    private final EmployeeRepo employeeRepo;
    private final AnswerProducer answerProducer;

    @Bean("telegramView")
    public EmployeeTelegramView telegramView() {
        return new EmployeeTelegramView(
                messageUtil()
        );
    }

    private MessageUtil messageUtil() {
        return new MessageUtil();
    }


    @Bean
    public Main main() {
        return new MainService(
                appUserRepo,
                answerProducer,
                wrongAppUserRoleUpdateProcessor(),
                notRegisteredAppUserUpdateProcessor(),
                commandUpdateProcessor(),
                callbackQueryUpdateProcessor()
                );
    }

    /**
     * UpdateProcessors
     */
    @Bean
    public UpdateProcessor<Update, AppUser> wrongAppUserRoleUpdateProcessor() {
        return new WrongAppUserRoleEmployeeUP(
                telegramView()
        );
    }

    @Bean
    public UpdateProcessor<Update, AppUser> notRegisteredAppUserUpdateProcessor() {
        return new NotRegisteredAppUserEmployeeUP(
                telegramView()
        );
    }

    @Bean
    public UpdateProcessor<Update, Long> nullableStateUpdateProcessor() {
        return new NullableStateEmployeeUP(
                employeeRepo,
                telegramView()
        );
    }

    @Bean(name = "previousViewUpdateProcessor")
    public UpdateProcessor<Update, EmployeeView> previousViewUpdateProcessor() {
        return new PreviousViewEmployeeUP(
                telegramView()
        );
    }

    @Bean(name = "studyStatePreviousViewUpdateProcessor")
    public UpdateProcessor<Update, EmployeeView> studyStatePreviousViewUpdateProcessor() {
        return new StudyStatePreviousViewUP(
                appContext.getBean("itemChoiceViewResolver", UpdateProcessor.class)
        );
    }

    /**
     * CommandUpdateProcessors
     */



    @Bean
    public UpdateProcessor<Update, AppUser> commandUpdateProcessor() {
        return new CommandEmployeeUP(
                nullableStateUpdateProcessor(),
                previousViewUpdateProcessor(),
                appContext.getBean("stateCommandDispatcher", Map.class)
        );
    }

    @Bean
    public Map<String, UpdateProcessor<Update, Long>> basicStateCommandDispatcher() {
        Map<String, UpdateProcessor<Update, Long>> dispatcher = new HashMap<>();

        dispatcher.put("/start", appContext.getBean("welcomeViewResolver", UpdateProcessor.class));

        return dispatcher;
    }

    @Bean(name = "basicStateCommandUpdateProcessor")
    public UpdateProcessor<Update, EmployeeView> basicStateCommandUpdateProcessor() {
        return new BasicStateEmployeeCUP(
                previousViewUpdateProcessor(),
                basicStateCommandDispatcher()
        );
    }

    /**
     * CallbackQueryUpdateProcessors
     * */

    @Bean
    public UpdateProcessor<Update, AppUser> callbackQueryUpdateProcessor() {
        return new CallbackQueryEmployeeUP(
                nullableStateUpdateProcessor(),
                previousViewUpdateProcessor(),
                appContext.getBean("stateCallbackQueryDispatcher", Map.class)
        );
    }

    @Bean(name = "basicStateCallbackQueryUpdateProcessor")
    public UpdateProcessor<Update, EmployeeView> basicStateCallbackQueryUpdateProcessor() {
        return new BasicStateEmployeeCQUP(
                previousViewUpdateProcessor(),
                appContext.getBean("basicStateCallbackQueryCommandDispatcher", Map.class)
        );
    }

    // TODO не доделаны ни мапа, ни процессор
    @Bean(name = "studyStateCallbackQueryUpdateProcessor")
    public UpdateProcessor<Update, EmployeeView> studyStateCallbackQueryUpdateProcessor() {
        return new StudyStateEmployeeCQUP(
                studyStatePreviousViewUpdateProcessor(),
                appContext.getBean("studyStateCallbackQueryCommandDispatcher", Map.class)
        );
    }
}
