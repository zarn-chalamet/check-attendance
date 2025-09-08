package com.ai.attendance_check.notification_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfig {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.queues.name:}")
    private String queue;

    @Value("${rabbitmq.routing.key}")
    private String routingKye;

    @Bean
    public Queue warningEmailQueue() {
        return new Queue(queue,true);
    }

    @Bean
    public DirectExchange warningEmailExchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding warningEmailBinding(Queue warningEmailQueue,
                                       DirectExchange warningEmailExchange) {
        return BindingBuilder.bind(warningEmailQueue)
                .to(warningEmailExchange)
                .with(routingKye);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
