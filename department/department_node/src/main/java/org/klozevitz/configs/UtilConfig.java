package org.klozevitz.configs;

import org.klozevitz.services.implementations.util.ExcelToTestParser;
import org.klozevitz.services.implementations.util.TestGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilConfig {
    @Bean(name = "excelParser")
    public ExcelToTestParser excelParser() {
        return new ExcelToTestParser();
    }

    @Bean(name = "testGenerator")
    public TestGenerator testGenerator() {
        return new TestGenerator();
    }
}
