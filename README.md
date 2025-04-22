# 🚀 Microsserviço de Gestão de Propostas de Empréstimo

Este projeto demonstra **comunicação assíncrona entre microsserviços** utilizando **RabbitMQ** e **AWS SNS**, garantindo alta escalabilidade e eficiência na gestão de propostas de empréstimo.

---

## 🔹 Visão Geral do Projeto

O sistema é composto por **três microsserviços**, que interagem de forma assíncrona:

- **📌 MS Proposta:** Recebe a solicitação de empréstimo e registra a proposta no banco de dados.  
- **📌 MS Notificação:** Envia alertas ao cliente via **AWS SNS**, notificando sobre o status da proposta (pendente/concluída).  
- **📌 MS Análise-Crédito:** Avalia a solicitação com base em critérios como **score do cliente, histórico financeiro e presença no Serasa**.

✅ A cada aprovação, pontos são somados. Se o cliente ultrapassar a pontuação mínima, seu empréstimo **é automaticamente aprovado**.

---

## 💡 Tecnologias Utilizadas

- ✅ **Java 21**  
- ✅ **Spring Boot** (Spring Web, Spring Data JPA, Spring Cloud)  
- ✅ **RabbitMQ** (Mensageria assíncrona)  
- ✅ **AWS SNS** (Notificação por SMS)  
- ✅ **PostgreSQL** (Banco de dados relacional)  
- ✅ **Docker** (Conteinerização e gerenciamento de serviços)  

---

## 📌 Como Executar o Projeto

### 🏗 Pré-requisitos

- Java 21+ instalado  
- Docker e Docker Compose instalados  
- Conta AWS configurada com SNS (ou usar mock local para testes)

---

### 🔧 Passo a passo

1. **Clone o repositório**
   ```bash
   git clone https://github.com/DouglasSimoesM/microsservico-propostas.git
   cd microsservico-propostas
Suba os serviços no Docker

bash
Copiar
Editar
docker-compose up -d
Execute os microsserviços

bash
Copiar
Editar
./gradlew bootRun
Acesse a documentação Swagger

http://localhost:8080/swagger-ui/

🔥 Diferenciais do Projeto

✅ Arquitetura de microsserviços com RabbitMQ e AWS SNS

✅ Processamento assíncrono para alta escalabilidade

✅ Uso de PostgreSQL para dados e MongoDB para logs

✅ Autenticação e segurança seguindo boas práticas

✅ Deploy via Docker

✅ Testes automatizados

✅ Documentação clara com Swagger


📂 Microsserviços Relacionados

🔹 MS Notificação: https://github.com/DouglasSimoesM/ms-notificar 

🔹 MS Análise-Crédito: https://github.com/DouglasSimoesM/ms-analise-credito

🤝 Contato & LinkedIn

🔹 GitHub: [DouglasSimoesM](https://github.com/DouglasSimoesM)

🔹 LinkedIn: [Douglas Simoes Maciel](https://www.linkedin.com/in/douglassimoes-maciel/)

Se curtir o projeto, ⭐ marque o repositório e me adicione no LinkedIn!
Estou sempre aberto a oportunidades como Desenvolvedor Backend Java. 😊
