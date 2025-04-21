package com.goulas.proposta.app.service;

import com.goulas.proposta.app.entity.Proposta;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DeadLetterQueueSchedulerService {

    private RabbitTemplate rabbitTemplate;

    public void notificarDeadAnaliseCredito(Proposta proposta, String exchange){
        rabbitTemplate.convertAndSend(exchange,"", proposta);
    }
    public void notificarDeadNotificacao(Proposta proposta, String exchange){
        rabbitTemplate.convertAndSend(exchange,"", proposta);
    }
}
