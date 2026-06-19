# Sonata API — Backend (Java + Spring Boot + PostgreSQL)

API REST do **Projeto Sonata** — serviço de streaming focado em música clássica.
Desenvolvida em **Java 21 + Spring Boot 3.3**, aplicando os princípios de
**Programação Orientada a Objetos** e organizada em camadas (model / repository
/ service / controller / dto / exception), como visto em sala.

A persistência usa **PostgreSQL** (provisionado por Docker e acessível pelo
DBeaver na porta `5432`). O Spring Data JPA + Hibernate **criam e atualizam
todas as tabelas e relacionamentos automaticamente** a partir das classes
`@Entity` (`spring.jpa.hibernate.ddl-auto=update`). Não é preciso modelar o
banco manualmente — sem `CREATE TABLE`, sem triggers, sem SQL escrito à mão.

---

## Stack

| Camada    | Tecnologia                                            |
|-----------|-------------------------------------------------------|
| Linguagem | Java 21                                               |
| Framework | Spring Boot 3.3 (Web, Data JPA, Validation, Security) |
| ORM       | Hibernate (via Spring Data JPA)                       |
| Banco     | **PostgreSQL 16** (container Docker)                  |
| Build     | Maven                                                 |
| Container | Docker + docker-compose                               |
| Util      | Lombok                                                |

---

## Aplicação de POO

- **Encapsulamento** — atributos privados nas entidades, acessados via
  getters/setters gerados pelo Lombok.
- **Abstração** — cada `Repository` é uma interface que estende
  `JpaRepository<Entidade, ID>`; o Spring gera a implementação em runtime.
- **Herança / composição** — entidades JPA compõem relacionamentos
  (`Composer` 1—N `Work`, `Work` 1—N `Movement`, `Playlist` N—N `Movement`,
  `User` 1—1 `Subscription`, `User` 1—N `PlayHistory`).
- **Polimorfismo** — serviços recebem dependências por interface (injeção
  via construtor), facilitando testes e substituição de implementações.
- **Separação de responsabilidades** — cada classe vive em uma única camada.

---

## Estrutura de pastas

```
backend/
├── pom.xml                     # dependências Maven (Spring Boot, PostgreSQL, JPA…)
├── Dockerfile                  # build da imagem Java da API
├── docker-compose.yml          # orquestra API + PostgreSQL
└── src/main/java/com/sonata/api/
    ├── SonataApiApplication.java
    ├── config/                 # SecurityConfig, CORS
    ├── model/                  # entidades JPA (@Entity)
    ├── repository/             # interfaces Spring Data JPA
    ├── service/                # regras de negócio
    ├── controller/             # endpoints REST (@RestController)
    ├── dto/                    # payloads de entrada/saída
    └── exception/              # tratamento global de erros
```

---

## Classes do domínio (todas principais)

Não há distinção entre classes "extras" e "principais" — todas compõem o
modelo de negócio do Sonata em pé de igualdade.

| Classe         | Papel                                                                 |
|----------------|-----------------------------------------------------------------------|
| `User`         | Ouvinte do serviço (autenticação, preferência de qualidade de áudio)  |
| `Composer`     | Compositor (Beethoven, Mozart…) e seu período histórico               |
| `Work`         | Obra musical (ex.: Sinfonia nº 5, Op. 67) ligada a um `Composer`      |
| `Movement`     | Movimento de uma obra (ordem garante *gapless playback*)              |
| `Playlist`     | Coleção pessoal do usuário, N—N com `Movement` (máx. 100 itens)       |
| `Subscription` | Plano do usuário (FREE / PREMIUM / LOSSLESS) — libera áudio FLAC      |
| `PlayHistory`  | Histórico de reprodução — base para "tocados recentes" e estatísticas |

O Hibernate gera automaticamente as tabelas `user`, `composer`, `work`,
`movement`, `playlist`, `playlist_movements` (join N—N), `subscription` e
`play_history`, com chaves estrangeiras e índices declarados nas anotações.

---

## Regras de negócio implementadas (além dos CRUDs)

- **Cadastro de usuário** — e-mail único; senha armazenada com **BCrypt**.
- **Obras (`Work`)** — `opusNumber` é obrigatório (diferencial do catálogo
  erudito).
- **Movimentos** — sempre listados em ordem (`movementOrder ASC`) para
  suportar *gapless playback*.
- **Playlist** — vinculada ao usuário dono; o serviço bloqueia duplicatas
  e respeita o limite de 100 movimentos.
- **Subscription**
  - `subscribe(userId, plan)` → cria ou atualiza o plano (upsert),
    `startDate = hoje`, `status = ACTIVE`.
  - `cancel(userId)` → marca `CANCELLED` e grava `endDate`.
- **PlayHistory**
  - `register(userId, movementId, segundos)` → registra cada execução.
  - `recent(userId, N)` → últimas N reproduções (ordem desc).
- **Tratamento de erros** — `GlobalExceptionHandler` converte
  `NotFoundException` em **404** e `BusinessException` em **400**, com
  payload JSON padronizado.

---

## Como rodar — Docker (PostgreSQL + API)

Pré-requisito: Docker Desktop / Docker Engine.

```bash
cd backend
docker compose up --build
```

Sobem dois containers:

| Container    | Porta | Detalhes                                                |
|--------------|-------|---------------------------------------------------------|
| `sonata-db`  | 5432  | PostgreSQL 16 · db `sonata` · user `sonata` / `sonata`  |
| `sonata-api` | 8080  | Spring Boot conectada via `jdbc:postgresql://sonata-db:5432/sonata` |

Derrubar:

```bash
docker compose down       # mantém os dados
docker compose down -v    # zera o volume do banco
```

### Conectar o DBeaver

1. **New Connection → PostgreSQL**
2. Host: `localhost` · Port: `5432`
3. Database: `sonata` · User: `sonata` · Password: `sonata`
4. **Test Connection → Finish**

As tabelas geradas pelo Hibernate aparecem em `sonata → Schemas → public → Tables`
assim que a API sobe pela primeira vez.

### Rodar a API fora do Docker (com Postgres do compose)

Útil em desenvolvimento. Suba só o banco (`docker compose up sonata-db`) e
depois:

```bash
./mvnw spring-boot:run
```

A API conecta em `jdbc:postgresql://localhost:5432/sonata` (default do
`application.properties`).

---

## Testando os endpoints com Postman

Todos os exemplos abaixo assumem que a API está rodando em
`http://localhost:8080`. Recomendamos criar uma **Environment** no Postman
com a variável `{{baseUrl}} = http://localhost:8080` e usá-la nas requisições.

### Configuração inicial no Postman

1. Abra o Postman → **New → Collection** → nomeie como `Sonata API`.
2. **New → Environment** → adicione a variável `baseUrl` com valor
   `http://localhost:8080` e selecione esse environment no canto superior direito.
3. Em cada request que envia JSON, vá na aba **Headers** e adicione:
   - `Content-Type: application/json`
4. O corpo JSON vai na aba **Body → raw → JSON**.

### Requisições principais

#### 1. Criar usuário
- **Method:** `POST`
- **URL:** `{{baseUrl}}/api/users`
- **Headers:** `Content-Type: application/json`
- **Body (raw / JSON):**
```json
{
  "name": "Ana",
  "email": "ana@sonata.com",
  "password": "secret123",
  "preferredAudioQuality": "LOSSLESS"
}
```
- **Resposta esperada:** `201 Created` com o usuário criado (anote o `id`).

#### 2. Listar compositores
- **Method:** `GET`
- **URL:** `{{baseUrl}}/api/composers`
- **Resposta esperada:** `200 OK` com array JSON de compositores.

#### 3. Criar compositor
- **Method:** `POST`
- **URL:** `{{baseUrl}}/api/composers`
- **Headers:** `Content-Type: application/json`
- **Body:**
```json
{
  "name": "Ludwig van Beethoven",
  "period": "CLASSICAL"
}
```

#### 4. Assinar plano (Subscription)
- **Method:** `POST`
- **URL:** `{{baseUrl}}/api/subscriptions/user/1`
- **Params (aba Params):**
  - `plan` = `PREMIUM`  *(valores válidos: `FREE`, `PREMIUM`, `LOSSLESS`)*
- **Body:** vazio.
- **Resposta esperada:** `200 OK` com a subscription ativa.

#### 5. Cancelar assinatura
- **Method:** `DELETE`
- **URL:** `{{baseUrl}}/api/subscriptions/user/1`

#### 6. Registrar reprodução (PlayHistory)
- **Method:** `POST`
- **URL:** `{{baseUrl}}/api/history`
- **Params:**
  - `userId` = `1`
  - `movementId` = `1`
  - `listenedSeconds` = `120`
- **Body:** vazio.

#### 7. Reproduções recentes
- **Method:** `GET`
- **URL:** `{{baseUrl}}/api/history/user/1/recent`
- **Params:**
  - `limit` = `10`

### Fluxo de teste sugerido

Execute as requisições nesta ordem para popular o banco e validar as regras
de negócio de ponta a ponta:

1. `POST /api/users` → cria o usuário (guarde o `id`).
2. `POST /api/composers` → cria um compositor.
3. `POST /api/works` → cria uma obra ligada ao compositor.
4. `POST /api/movements` → cria os movimentos da obra.
5. `POST /api/subscriptions/user/{id}?plan=PREMIUM` → ativa o plano.
6. `POST /api/playlists` → cria a playlist do usuário.
7. `POST /api/history?userId=...&movementId=...&listenedSeconds=...` → registra reprodução.
8. `GET /api/history/user/{id}/recent?limit=10` → confirma o histórico.

### Dica — salvar IDs entre requisições

Na aba **Tests** de uma request `POST`, você pode capturar o `id` da resposta
e guardar como variável do environment para reusar nas próximas chamadas:

```javascript
const data = pm.response.json();
pm.environment.set("userId", data.id);
```

Depois é só usar `{{userId}}` na URL das próximas requisições
(ex.: `{{baseUrl}}/api/subscriptions/user/{{userId}}`).

---

## Onde o Docker entra (resumo)

- `backend/Dockerfile` — build multi-stage: compila o JAR (Maven + JDK 21) e
  roda numa imagem `eclipse-temurin:21-jre` enxuta.
- `backend/docker-compose.yml` — orquestra `sonata-db` (PostgreSQL 16, com
  `healthcheck` via `pg_isready`) + `sonata-api`, garantindo que a API só
  suba depois que o banco aceite conexões.
- Volume nomeado `sonata_db_data` persiste os dados entre reinícios.

