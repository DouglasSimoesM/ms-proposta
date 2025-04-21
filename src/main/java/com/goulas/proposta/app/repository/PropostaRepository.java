package com.goulas.proposta.app.repository;

import com.goulas.proposta.app.entity.Proposta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropostaRepository extends CrudRepository<Proposta, Long> {

    List<Proposta> findAllByIntegradaIsFalse();
    List<Proposta> findAllByAnaliseCreditoFailIsFalse();
    List<Proposta> findAllByNotificacaoFailIsFalse();
}
