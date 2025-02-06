package org.klozevitz.configs;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.klozevitz.RabbitQueue.*;

@Configuration
public class RabbitMQConfig {

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue textMessageQueue() {
        return new Queue(DEPARTMENT_TEXT_UPDATE);
    }

    @Bean
    public Queue commandUpdate() {
        return new Queue(DEPARTMENT_COMMAND_UPDATE);
    }

    @Bean
    public Queue callbackQueryUpdate() {
        return new Queue(DEPARTMENT_CALLBACK_QUERY_UPDATE);
    }

    @Bean
    public Queue docUpdate() {
        return new Queue(DEPARTMENT_DOC_UPDATE);
    }

    @Bean
    public Queue answerMessageUpdate() {return new Queue(DEPARTMENT_ANSWER_MESSAGE);}
}
