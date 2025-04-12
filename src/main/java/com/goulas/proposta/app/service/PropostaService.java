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

    private PropostaRepository propostaRepository;

    private NotificacaoRabbitService notificacaoRabbitService;

    private String exchange;

    public PropostaService(PropostaRepository propostaRepository,
                           NotificacaoRabbitService notificacaoRabbitService,
                           @Value("${rabbitmq.propostapendente.exchange}") String exchange) {
        this.propostaRepository = propostaRepository;
        this.notificacaoRabbitService = notificacaoRabbitService;
        this.exchange = exchange;
    }

    public PropostaResponseDto criar(PropostaRequestDto requestDto){

        Proposta proposta = PropostaMapper.INSTANCE.converterDtotoProposta(requestDto);
        propostaRepository.save(proposta);

        notificarRabbitMq(proposta);

        notificacaoRabbitService.notificar(proposta,exchange);

        return PropostaMapper.INSTANCE.convertEntityToDto(proposta);

    }

    private void notificarRabbitMq(Proposta proposta){

        try {
            notificacaoRabbitService.notificar(proposta, exchange);
        } catch (RuntimeException e) {
            proposta.setIntegrada(false);
            propostaRepository.save(proposta);
        }
    }

    public List<PropostaResponseDto> obterProposta() {
        return PropostaMapper.INSTANCE.convertListEntityToListDto(propostaRepository.findAll());
    }
}
