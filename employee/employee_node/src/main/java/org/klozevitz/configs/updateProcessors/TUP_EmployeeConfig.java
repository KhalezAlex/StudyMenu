package org.klozevitz.configs.updateProcessors;

import lombok.RequiredArgsConstructor;
import org.klozevitz.services.implementations.updateProcessors.text.TextEmployeeUP;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TUP_EmployeeConfig {
    private final ApplicationContext appContext;

    @Bean(name = "textEmployeeUpdateProcessor")
    public UpdateProcessor textEmployeeUpdateProcessor() {
        return new TextEmployeeUP(
                appContext.getBean("previousViewUpdateProcessor", UpdateProcessor.class)
        );
    }
}
