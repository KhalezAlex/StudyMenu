package org.klozevitz.configs;

//@Configuration
//@RequiredArgsConstructor
public class AppConfigLegacy {
//    private final AppUserRepo appUserRepo;
//    private final AnswerProducer answerProducer;
//
//    @Bean
//    public MessageUtil messageUtil() {
//        return new MessageUtil();
//    }
//
//    @Bean
//    public EmployeeTelegramView telegramView() {
//        return new EmployeeTelegramView(
//                new MessageUtil()
//        );
//    }
//
//    @Bean
//    public Main main() {
//        return new MainService(
//                appUserRepo,
//                answerProducer,
//                wrongAppUserRoleUpdateProcessor(),
//                notRegisteredAppUserUpdateProcessor(),
//                commandUpdateProcessor(),
//                callbackQueryUpdateProcessor(),
//                textUpdateProcessor()
//        );
//    }
//
//    /**
//     * Update-обработчики
//     * */
//    @Bean
//    public WrongAppUserDataUpdateProcessor wrongAppUserRoleUpdateProcessor() {
//        return new WrongAppUserRoleEmployeeUP(
//                telegramView()
//        );
//    }
//
//    @Bean
//    public WrongAppUserDataUpdateProcessor notRegisteredAppUserUpdateProcessor() {
//        return new NotRegisteredAppUserEmployeeUP(
//                telegramView()
//        );
//    }
//
//    @Bean
//    public UpdateProcessor nullableStateUpdateProcessor() {
//        return new NullableStateEmployeeUP(
//                appUserRepo,
//                telegramView()
//        );
//    }
//
//    @Bean
//    public UpdateProcessor previousViewUpdateProcessor() {
//        return new PreviousViewEmployeeUP(
//                telegramView()
//        );
//    }
//
//    /**
//     * CommandUpdate-обработчики
//     * */
//    @Bean
//    public UpdateProcessor commandUpdateProcessor() {
//        return new CommandEmployeeUP(
//                nullableStateUpdateProcessor(),
//                basicStateCommandUpdateProcessor(),
//                previousViewUpdateProcessor()
//        );
//    }
//
//    @Bean
//    public UpdateProcessor basicStateCommandUpdateProcessor() {
//        return new BasicStateEmployeeCUP(
//                appUserRepo,
//                telegramView(),
//                previousViewUpdateProcessor()
//        );
//    }
//
//    /**
//     * CallbackQueryUpdate-обработчики
//     * */
//    @Bean
//    public UpdateProcessor callbackQueryUpdateProcessor() {
//        return new CallbackQueryEmployeeUP(
//                nullableStateUpdateProcessor(),
//                basicStateCallbackQueryUpdateProcessor(),
//                previousViewUpdateProcessor()
//        );
//    }
//
//    @Bean
//    public UpdateProcessor basicStateCallbackQueryUpdateProcessor() {
//        return new BasicStateEmployeeCQUP(
//                appUserRepo,
//                telegramView(),
//                previousViewUpdateProcessor()
//        );
//    }
//
//    /**
//     * TextUpdate-обработчики
//     * */
//    @Bean
//    public UpdateProcessor textUpdateProcessor() {
//        return new TextEmployeeUP();
//    }
}
