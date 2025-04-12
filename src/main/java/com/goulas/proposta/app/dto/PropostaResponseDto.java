package com.goulas.proposta.app.dto;

public record PropostaResponseDto(Long id, String nome, String sobrenome, String telefone, String cpf, Double renda, String valorSolicitado,boolean integrada,int prazoPagamento, String observacao, Boolean aprovada) {
}
