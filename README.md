# Authentication Provider — OAuth2 / OIDC Authorization Server

Um **Authorization Server OAuth2 / OpenID Connect** construído com **Spring Authorization Server**, com cadastro e verificação de usuários por e-mail e registro dinâmico de clients OAuth2.

## Stack

- **Java 17**
- **Spring Boot 4.1.0**
- **Spring Security** + **Spring Authorization Server** (`spring-boot-starter-security-oauth2-authorization-server`), com suporte a **OIDC**
- **Spring Data JPA** + **PostgreSQL**
- **Flyway** para migrações de banco
- **Thymeleaf** (página de registro de client)
- **Spring Mail** (envio de e-mail de verificação via SMTP)
- Tokens JWT assinados com **RS256**, chave carregada de um keystore Java (`.jks`)
- **Lombok**
- **Maven** (com wrapper incluso)

## O que o projeto faz

- Funciona como **servidor de autorização OAuth2/OIDC**: expõe os endpoints padrão do protocolo (`/oauth2/authorize`, `/oauth2/token`, `/.well-known/openid-configuration`, JWKS, etc.) através do `OAuth2AuthorizationServerConfiguration`.
- Permite **cadastro de usuários** (`/auth/signup`) com senha criptografada via BCrypt.
- Envia um **e-mail de verificação** (template HTML) com um código, e permite confirmar a conta em `/auth/verify`.
- Permite **registro dinâmico de clients OAuth2** (`/connect/register`), gerando `client_id`, `client_secret` e um **registration access token**.
- Assina os tokens JWT emitidos usando uma chave **RSA** carregada de um keystore (`privateK.jks`).

## Estrutura do projeto

```
src/main/java/com/github/BMP29/oauth2_auth_provider/
├── Oauth2AuthProviderApplication.java   # Classe principal (entry point)
├── config/
│   └── SecurityConfig.java              # Authorization Server, filtros, JWK/JWT, mail, client repo
├── controller/
│   ├── AuthenticationController.java    # /auth (signup, verify)
│   ├── ClientController.java            # /connect/register (registro de clients OAuth2)
│   └── view/
│       └── ClientPageController.java    # /register (página Thymeleaf de registro de client)
├── dto/                                 # Records de entrada/saída
├── entity/
│   ├── User.java                        # Entidade JPA, implementa UserDetails
│   └── RegistrationAccessToken.java     # Token de acesso ao registro de client
├── mapper/
│   └── UserMapper.java
├── repository/
│   ├── UserRepository.java
│   └── RegistrationAccessTokenRepository.java
└── service/
    ├── I*.java                          # Interfaces dos serviços
    └── Impl/
        ├── AuthenticationServiceImpl.java
        ├── ClientServiceImpl.java
        ├── EmailServiceImpl.java
        ├── RegistrationAccessTokenServiceImpl.java
        └── UserServiceImpl.java

src/main/resources/
├── application.properties
└── db/migration/
    └── V1__create_initial_tables.sql    # Cria oauth2_registered_client, registration_access_token, users
```

## Como rodar

### Pré-requisitos
- JDK 17+
- PostgreSQL rodando (por padrão, banco `spring` em `localhost:5432`)
- Um keystore Java (`.jks`) com um par de chaves RSA, disponível em `src/main/resources/privateK.jks` (**não incluído no repositório** — precisa ser gerado)
- Uma conta Gmail (ou outro SMTP compatível) para envio do e-mail de verificação

Para gerar um keystore de teste rapidamente:

```bash
keytool -genkeypair -alias meu-alias -keyalg RSA -keysize 2048 \
  -storetype JKS -keystore src/main/resources/privateK.jks -validity 3650
```

### Configuração

O projeto lê `dbpassword`, `keypassword`, `alias`, `SUPPORT_EMAIL` e `APP_PASSWORD` como placeholders em `application.properties`. Configure-os como variáveis de ambiente ou sobrescreva o arquivo localmente:

| Propriedade / variável | Descrição |
|---|---|
| `spring.datasource.url` | URL JDBC do PostgreSQL (padrão: `jdbc:postgresql://localhost:5432/spring`) |
| `spring.datasource.username` | Usuário do banco (padrão: `postgres`) |
| `dbpassword` | Senha do banco |
| `app.keystore.location` | Local do keystore (padrão: `classpath:privateK.jks`) |
| `keypassword` | Senha do keystore e da chave |
| `alias` | Alias da chave RSA dentro do keystore |
| `SUPPORT_EMAIL` | E-mail remetente (SMTP) |
| `APP_PASSWORD` | Senha/app password do e-mail remetente |

> `spring.jpa.hibernate.ddl-auto=validate` — o schema é criado pelas migrações do **Flyway**, não pelo Hibernate.

### Subindo a aplicação

```bash
# Linux/macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

A aplicação sobe por padrão na porta `8080`. As migrações do Flyway rodam automaticamente na inicialização.

### Rodando os testes

```bash
./mvnw test
```

## Endpoints da API

### Autenticação — `/auth` (público)

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/auth/signup` | Cria uma nova conta (usuário fica desabilitado até verificar o e-mail) |
| `POST` | `/auth/verify` | Verifica a conta usando o código enviado por e-mail |

### Registro de clients OAuth2 — `/connect`

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/connect/register` | Registra um novo client OAuth2, retornando `clientId`, `clientSecret` e `registrationAccessToken` |
| `GET` | `/register` | Página Thymeleaf para registro de client via navegador |

### Endpoints do Authorization Server (OIDC)

Expostos automaticamente pelo Spring Authorization Server, incluindo, entre outros:

- `GET /.well-known/openid-configuration` — metadados OIDC
- `GET /oauth2/jwks` — chaves públicas (JWKS) para validar os tokens
- `GET /oauth2/authorize` — endpoint de autorização
- `POST /oauth2/token` — emissão de tokens
- `GET /login` — tela de login (form login) usada pelo fluxo de autorização

Todas as demais rotas exigem autenticação (`anyRequest().authenticated()`), exceto `/auth/*`, que é público.

## Modelo de dados (resumo)

- **users**: `id`, `username`, `password` (hash BCrypt), `email`, `enabled`, `created_at`, `last_updated`, `verification_code`, `verification_code_expires_at`
- **oauth2_registered_client**: tabela padrão do Spring Authorization Server (`JdbcRegisteredClientRepository`), armazena os clients OAuth2 registrados
- **registration_access_token**: `id`, `client_id`, `token_hash`, `expires_at`, `created_at`, `revoked`

## Status / próximos passos

- `UserServiceImpl` ainda está vazio — funcionalidades de gerenciamento de usuário autenticado (perfil, troca de senha etc.) ainda não foram implementadas

## Licença

Este projeto está sob a licença [MIT](LICENSE).
