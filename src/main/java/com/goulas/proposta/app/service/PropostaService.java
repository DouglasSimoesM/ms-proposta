package com.goulas.proposta.app.service;

import com.goulas.proposta.app.dto.PropostaRequestDto;
import com.goulas.proposta.app.dto.PropostaResponseDto;
import com.goulas.proposta.app.entity.Proposta;
import com.goulas.proposta.app.mapper.PropostaMapper;
import com.goulas.proposta.app.repository.PropostaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropostaService {

    private final PropostaRepository propostaRepository;

    private final NotificacaoRabbitService notificacaoRabbitService;

    private final DeadLetterQueueSchedulerService deadLetterQueueSchedulerService;

    private final String exchange;

    private final String exchangeDead;

    public PropostaService(PropostaRepository propostaRepository,
                           NotificacaoRabbitService notificacaoRabbitService,
                           DeadLetterQueueSchedulerService deadLetterQueueSchedulerService,
                           @Value("${rabbitmq.propostapendente.exchange}") String exchange,
                           @Value("${rabbitmq.propostapendentedlx.exchange}")String exchangeDead) {
        this.propostaRepository = propostaRepository;
        this.notificacaoRabbitService = notificacaoRabbitService;
        this.deadLetterQueueSchedulerService = deadLetterQueueSchedulerService;
        this.exchange = exchange;
        this.exchangeDead = exchangeDead;
    }

    public PropostaResponseDto criar(PropostaRequestDto requestDto){

        Proposta proposta = PropostaMapper.INSTANCE.converterDtotoProposta(requestDto);
        propostaRepository.save(proposta);

        notificacaoRabbitService.notificar(proposta,exchange);
        deadLetterQueueSchedulerService.notificarDeadAnaliseCredito(proposta, exchangeDead);
        deadLetterQueueSchedulerService.notificarDeadNotificacao(proposta, exchangeDead);

        return PropostaMapper.INSTANCE.convertEntityToDto(proposta);

    }

    public List<PropostaResponseDto> obterProposta() {
        return PropostaMapper.INSTANCE.convertListEntityToListDto(propostaRepository.findAll());
    }
}
