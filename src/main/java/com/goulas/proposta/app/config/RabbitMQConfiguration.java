package com.goulas.proposta.app.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    @Value("${rabbitmq.propostapendente.exchange}")
    private String exchangePropostaPendente;

    @Value("${rabbitmq.propostaconcluida.exchange}")
    private String exchangePropostaConcluida;

    @Value("${rabbitmq.propostapendentedlx.exchange}")
    private String exchangePropostaPendenteDql;

    @Bean
    public Queue criarFilaPropostaPendenteMsAnaliseCredito() {
        return QueueBuilder.durable("proposta-pendente.ms-analise-credito")
                .withArgument("x-message-ttl", 15000)  // Define TTL de 15 segundos
                .deadLetterExchange("proposta-pendente-dlx.ex")
                .deadLetterRoutingKey("proposta-pendente-analise-credito.dlq")  // Define a rota para DLX
                .build();
    }

    @Bean
    public Queue criarFilaPropostaPendenteMsNotificacao() {
        return QueueBuilder.durable("proposta-pendente.ms-notificacao")
                .withArgument("x-message-ttl", 15000)  // Define TTL de 15 segundos
                .deadLetterExchange("proposta-pendente-dlx.ex")
                .deadLetterRoutingKey("proposta-pendente-notificacao.dlq")  // Define a rota para DLX
                .build();
    }

    @Bean
    public Queue criarFilaPropostaPendenteAnaliseCreditoDlq() {
        return QueueBuilder.durable("proposta-pendente-analise-credito.dlq").build();
    }

    @Bean
    public Queue criarFilaPropostaPendenteNotificacao() {
        return QueueBuilder.durable("proposta-pendente-notificacao.dlq").build();
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return ExchangeBuilder.directExchange(exchangePropostaPendenteDql).build();
    }

    @Bean
    public Binding criarBindingFilaPropostaPendenteAnaliseCreditoDlq() {
        return BindingBuilder.bind(criarFilaPropostaPendenteAnaliseCreditoDlq())
                .to(deadLetterExchange())
                .with("proposta-pendente-analise-credito.dlq");
    }

    @Bean
    public Binding criarBindingFilaPropostaPendenteNotificacao(){
        return BindingBuilder.bind(criarFilaPropostaPendenteNotificacao())
                .to(deadLetterExchange())
                .with("proposta-pendente-notificacao.dlq");
    }


    @Bean
    public Queue criarFilaPropostaConcluidaMsProposta() {
        return QueueBuilder.durable("proposta-concluida.ms-proposta").build();
    }

    @Bean
    public Queue criarFilaPropostaConcluidaMsNotificacao() {
        return QueueBuilder.durable("proposta-concluida.ms-notificacao").build();
    }

    @Bean
    public RabbitAdmin criarRabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicializarAdmin(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public FanoutExchange criarFanoutExchangePropostaPendente() {
        return ExchangeBuilder.fanoutExchange(exchangePropostaPendente).build();
    }

    @Bean
    public FanoutExchange criarFanoutExchangePropostaConcluida() {
        return ExchangeBuilder.fanoutExchange(exchangePropostaConcluida).build();
    }

    @Bean
    public Binding criarBindingPropostaPendenteMSAnaliseCredito() {
        return BindingBuilder.bind(criarFilaPropostaPendenteMsAnaliseCredito()).
                to(criarFanoutExchangePropostaPendente());
    }

    @Bean
    public Binding criarBindingPropostaPendenteMSNotificacao() {
        return BindingBuilder.bind(criarFilaPropostaPendenteMsNotificacao()).
                to(criarFanoutExchangePropostaPendente());
    }

    @Bean
    public Binding criarBindingPropostaConcluidaMSPropostaApp() {
        return BindingBuilder.bind(criarFilaPropostaConcluidaMsProposta()).
                to(criarFanoutExchangePropostaConcluida());
    }

    @Bean
    public Binding criarBindingPropostaConcluidaMSNotificacao() {
        return BindingBuilder.bind(criarFilaPropostaConcluidaMsNotificacao()).
                to(criarFanoutExchangePropostaConcluida());
    }

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());

        return rabbitTemplate;
    }
}