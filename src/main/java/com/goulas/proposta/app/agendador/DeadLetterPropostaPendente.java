package com.goulas.proposta.app.agendador;

import com.goulas.proposta.app.entity.Proposta;
import com.goulas.proposta.app.repository.PropostaRepository;
import com.goulas.proposta.app.service.DeadLetterQueueSchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class DeadLetterPropostaPendente {

    private final PropostaRepository propostaRepository;
    private final DeadLetterQueueSchedulerService deadLetterQueueSchedulerService;
    private final String exchange;
    private final Logger logger = LoggerFactory.getLogger(DeadLetterPropostaPendente.class);

    public DeadLetterPropostaPendente(PropostaRepository propostaRepository,
                                      DeadLetterQueueSchedulerService deadLetterQueueSchedulerService,
                                      @Value("${rabbitmq.propostapendentedlx.exchange}")String exchange) {
        this.propostaRepository = propostaRepository;
        this.deadLetterQueueSchedulerService = deadLetterQueueSchedulerService;
        this.exchange = exchange;
    }

    @Scheduled(fixedDelay = 20, timeUnit = TimeUnit.SECONDS)
    public void buscaranaliseCreditoFail(){
        propostaRepository.findAllByAnaliseCreditoFailIsFalse().forEach(proposta -> {
            try {
                deadLetterQueueSchedulerService.notificarDeadAnaliseCredito(proposta, exchange);
                atualizarPropostaCredito(proposta);
            } catch (RuntimeException e) {
                logger.error(e.getMessage());
            }
        });
    }
    @Scheduled(fixedDelay = 20, timeUnit = TimeUnit.SECONDS)
    public void buscarNotificacaoFail(){
        propostaRepository.findAllByNotificacaoFailIsFalse().forEach(proposta -> {
            try {
                deadLetterQueueSchedulerService.notificarDeadNotificacao(proposta, exchange);
                atualizarPropostaNotificacao(proposta);
            } catch (RuntimeException e) {
                logger.error(e.getMessage());
            }
        });
    }

    public void atualizarPropostaCredito(Proposta proposta){
        proposta.setAnaliseCreditoFail(false);
        propostaRepository.save(proposta);
    }

    public void atualizarPropostaNotificacao(Proposta proposta){
        proposta.setNotificacaoFail(true);
        propostaRepository.save(proposta);
    }
}
