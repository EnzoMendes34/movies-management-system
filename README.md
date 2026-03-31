# 🎬 FinalCut API

<p>
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"/>
  <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white"/>
  <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white"/>
  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white"/>
  <img src="https://img.shields.io/badge/Stripe-635BFF?style=for-the-badge&logo=stripe&logoColor=white"/>
  <img src="https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway&logoColor=white"/>
  <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black"/>
</p>

API RESTful para gerenciamento completo de um sistema de cinema, com controle de filmes, salas, sessões, reservas de assentos e pagamentos integrados via Stripe.

---

## 📋 Funcionalidades

- ✅ Autenticação e autorização com **JWT + Spring Security**
- ✅ CRUD completo de Filmes, Salas, Assentos e Sessões
- ✅ Reserva de assentos com controle de **concorrência (Lock Pessimista)**
- ✅ Integração com **Stripe** para processamento de pagamentos
- ✅ Confirmação automática de reserva via **Stripe Webhook**
- ✅ Expiração automática de reservas pendentes com **@Scheduled**
- ✅ Envio de **e-mails** de confirmação e cancelamento
- ✅ Paginação, filtros e busca nos endpoints
- ✅ **HATEOAS** — nível 3 do modelo de maturidade REST
- ✅ Migrations com **Flyway**
- ✅ Documentação com **Swagger / OpenAPI**
- ✅ Suporte a JSON, XML e YAML nos endpoints

---

## 🛠️ Tecnologias

| Tecnologia | Descrição |
|------------|-----------|
| Java 21 | Linguagem principal |
| Spring Boot 3.x | Framework principal |
| Spring Security 6.x | Autenticação e autorização |
| Spring Data JPA / Hibernate | Acesso ao banco de dados |
| PostgreSQL | Banco de dados relacional |
| Flyway | Migrations do banco |
| JWT (java-jwt) | Tokens de autenticação |
| Stripe | Gateway de pagamentos |
| JavaMailSender | Envio de e-mails |
| Swagger / OpenAPI 3 | Documentação da API |
| Dozer Mapper | Mapeamento entre entidades e DTOs |
| Maven | Gerenciamento de dependências |

---

## 🔐 Autenticação

A API utiliza **JWT** para autenticação. Para acessar os endpoints protegidos:

1. Crie um usuário em `POST /auth/signup`
2. Faça login em `POST /auth/signin` e copie o token retornado
3. Envie o token no header de todas as requisições:

```
Authorization: Bearer {seu_token}
```

---

## 📡 Endpoints

### 🔑 Auth
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/auth/signin` | Login e geração do token JWT |
| POST | `/auth/signup` | Cadastro de novo usuário |
| PUT | `/auth/refresh/{username}` | Refresh do token |

### 🎥 Filmes
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/movie/v1` | Lista todos os filmes (paginado) |
| GET | `/api/movie/v1/{id}` | Busca filme por ID |
| GET | `/api/movie/v1/search` | Busca filmes por título |
| POST | `/api/movie/v1` | Cria novo filme |
| PUT | `/api/movie/v1` | Atualiza filme |
| PATCH | `/api/movie/v1/{id}/disable` | Desativa filme |

### 🏛️ Salas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/room/v1` | Lista todas as salas (paginado) |
| GET | `/api/room/v1/enabled` | Lista salas ativas |
| GET | `/api/room/v1/{id}` | Busca sala por ID |
| POST | `/api/room/v1` | Cria nova sala e gera seus assentos automaticamente |
| PUT | `/api/room/v1` | Atualiza sala |
| PATCH | `/api/room/v1/{id}/disable` | Desativa sala |

### 🪑 Assentos
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/seat/v1/{id}` | Busca assento por ID |
| GET | `/api/seat/v1/room/{roomId}` | Lista assentos de uma sala |
| GET | `/api/seat/v1/available` | Lista assentos disponíveis por sessão |
| POST | `/api/seat/v1` | Cria novo assento |
| PUT | `/api/seat/v1` | Atualiza assento |
| DELETE | `/api/seat/v1/{id}` | Remove assento |

### 📅 Sessões
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/session/v1` | Lista todas as sessões (paginado) |
| GET | `/api/session/v1/{id}` | Busca sessão por ID |
| GET | `/api/session/v1/movie/{movieId}` | Lista sessões de um filme por status |
| POST | `/api/session/v1` | Cria nova sessão |
| PUT | `/api/session/v1` | Atualiza sessão |
| PATCH | `/api/session/v1/{id}/cancel` | Cancela sessão |

### 🎟️ Reservas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/reservation/v1/{id}` | Busca reserva por ID |
| GET | `/api/reservation/v1/user/{userId}` | Lista reservas do usuário |
| POST | `/api/reservation/v1` | Cria nova reserva |
| PATCH | `/api/reservation/v1/{id}/cancel` | Cancela reserva |

### 💳 Pagamentos
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/payment/v1/intent` | Cria PaymentIntent no Stripe |
| POST | `/api/payment/v1/webhook` | Recebe eventos do Stripe |
| GET | `/api/payment/v1/status/{reservationId}` | Consulta status do pagamento |

---

## 💳 Fluxo de Pagamento

```
Usuário seleciona assentos
         │
         ▼
POST /api/reservation/v1
→ Cria reserva com status PENDING
→ Assentos NÃO ocupados ainda
         │
         ▼
POST /api/payment/v1/create-intent
→ Cria PaymentIntent no Stripe
→ Retorna { clientSecret } para o front
         │
         ▼
Front processa pagamento via Stripe Elements
         │
         ▼
Stripe → POST /api/payment/v1/webhook
         │
    ┌────┴────┐
    │         │
  SUCESSO   FALHA
    │         │
    ▼         ▼
Reserva    Reserva
CONFIRMED  CANCELLED
Assentos   Assentos
ocupados   liberados
    │
    ▼
E-mail de confirmação
```

---

## 🔒 Controle de Concorrência

Para evitar que dois usuários reservem o mesmo assento simultaneamente, a API utiliza **Lock Pessimista** (`PESSIMISTIC_WRITE`) no banco de dados:

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT s FROM Seat s WHERE s.id IN :ids")
List<Seat> findByIdInWithLock(@Param("ids") List<Long> ids);
```

Isso garante que apenas uma transação por vez possa reservar um determinado assento em uma sessão.

---

## ⚙️ Como rodar o projeto

### Pré-requisitos
- Java 21+
- Maven
- PostgreSQL ou conta no [Neon](https://neon.tech)
- Conta no [Stripe](https://stripe.com)
- Conta Gmail com App Password configurada

### Passo a passo

```bash
# Clone o repositório
git clone https://github.com/EnzoMendes34/movies-management-system.git

# Entre na pasta
cd movies-management-system
```

Configure as variáveis de ambiente:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://seu-host/seu-banco
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  mail:
    username: ${EMAIL_USERNAME}
    password: ${EMAIL_PASSWORD}

security:
  jwt:
    token:
      secret-key: ${JWT_SECRET}

stripe:
  secret-key: ${STRIPE_SECRET_KEY}
  webhook-secret: ${STRIPE_WEBHOOK_SECRET}
```

```bash
# Rode o projeto
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`

A documentação Swagger estará em `http://localhost:8080/swagger-ui/index.html`

---

## 🧪 Testando o Webhook do Stripe localmente

```bash
# Instale o Stripe CLI
stripe login
stripe listen --forward-to localhost:8080/api/payment/v1/webhook
```

---

## 👨‍💻 Autor

Feito por **Enzo Mendes**

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/enzo-mendes-49896b285)
[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/EnzoMendes34)
