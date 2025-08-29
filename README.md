# API de Gerenciamento de Carteira de Investimentos

Esta é uma API RESTful desenvolvida em **Java 17** com **Spring Boot 3** para gerenciar uma carteira de investimentos, permitindo o cadastro, visualização, atualização e remoção de ativos, além de fornecer um resumo consolidado da carteira.

## Requisitos Técnicos Utilizados

- **Java 17 e Spring Boot 3.x**: O projeto foi construído utilizando as versões mais recentes e estáveis do Java e do Spring Boot.
- **Spring Data JPA**: Para a camada de persistência, facilitando a comunicação com o banco de dados.
- **MySQL**: Banco de dados relacional escolhido para armazenar os dados.
- **Docker**: A forma recomendada para executar o banco de dados localmente é através de um container Docker, garantindo um ambiente consistente.
- **DTOs (Data Transfer Objects)**: Foram utilizados para separar a representação dos dados da API da estrutura da entidade do banco de dados, melhorando a segurança e a flexibilidade.
- **Arquitetura em Camadas**: O projeto segue uma arquitetura bem definida com as camadas:
    - `Controller`: Responsável por receber as requisições HTTP e retornar as respostas.
    - `Service`: Contém a lógica de negócio da aplicação.
    - `Repository`: Interface para acesso aos dados, utilizando o Spring Data JPA.
    - `Model`: Entidades que representam as tabelas do banco de dados.
    - `DTO`: Objetos para transferência de dados entre as camadas e o cliente.

---

## Como Executar Localmente

### Pré-requisitos

- Java 17 (ou superior)
- Maven 3.8+
- Docker e Docker Compose

### 1. Clonar o Repositório

```bash
git clone <URL_DO_SEU_REPOSITORIO>
cd <NOME_DO_DIRETORIO>
```

### 2. Iniciar o Banco de Dados com Docker

Para facilitar a configuração, você pode usar o Docker para iniciar uma instância do MySQL. O arquivo `docker-compose.yml` já está configurado na raiz do projeto com o seguinte conteúdo:

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    container_name: investments-db
    environment:
      MYSQL_ROOT_PASSWORD: admin
      MYSQL_DATABASE: investments_db
      MYSQL_USER: investments_user
      MYSQL_PASSWORD: admin
    ports:
      - "3307:3306"
    volumes:
      - mysql_data:/var/lib/mysql
    command: --default-authentication-plugin=mysql_native_password

volumes:
  mysql_data:
```

Depois, execute o seguinte comando para iniciar o container:

```bash
docker-compose up -d
```

O banco de dados MySQL estará disponível em `localhost:3307`.

### 3. Executar a Aplicação Spring Boot

Você pode executar a aplicação de duas formas:

**Via Maven:**

```bash
./mvnw spring-boot:run
```

**Via IDE:**

Importe o projeto como um projeto Maven em sua IDE preferida (IntelliJ, Eclipse, VS Code) e execute a classe principal `InvestmentsApplication.java`.

A API estará disponível em `http://localhost:8080`.

---

## Endpoints da API

### 1. Cadastrar um novo ativo

- **Endpoint**: `POST /investments`
- **Descrição**: Adiciona um novo ativo à carteira.
- **Payload**:
  ```json
  {
    "type": "ACAO",
    "symbol": "ITSA4",
    "quantity": 200,
    "purchasePrice": 10.50,
    "purchaseDate": "2025-08-15"
  }
  ```
- **Resposta (Sucesso - 201 Created)**:
  ```json
  {
    "id": 6,
    "type": "ACAO",
    "symbol": "ITSA4",
    "quantity": 200,
    "purchasePrice": 10.50,
    "purchaseDate": "2025-08-15",
    "totalValue": 2100.00
  }
  ```

### 2. Listar todos os ativos

- **Endpoint**: `GET /investments`
- **Descrição**: Retorna todos os ativos da carteira.

### 3. Filtrar ativos por tipo

- **Endpoint**: `GET /investments?type=CRIPTO`
- **Descrição**: Retorna todos os ativos do tipo especificado. Os tipos válidos são: `ACAO`, `CRIPTO`, `FUNDO`, `RENDA_FIXA`, `OUTRO`.
- **Exemplos de tipos**:
  - `ACAO`: Ações da bolsa (ex: PETR4, BBAS3)
  - `CRIPTO`: Criptomoedas (ex: BTC, ETH)
  - `FUNDO`: Fundos de investimento (ex: KNRI11)
  - `RENDA_FIXA`: Títulos de renda fixa (ex: TESOURO_SELIC_2029)
  - `OUTRO`: Qualquer outro tipo de investimento

### 4. Atualizar um ativo

- **Endpoint**: `PUT /investments/{id}`
- **Descrição**: Atualiza as informações de um ativo existente.
- **Payload**: Mesmo formato do `POST`.

### 5. Remover um ativo

- **Endpoint**: `DELETE /investments/{id}`
- **Descrição**: Remove um ativo da carteira pelo seu ID.
- **Resposta (Sucesso)**: `204 No Content`

### 6. Obter resumo da carteira

- **Endpoint**: `GET /investments/summary`
- **Descrição**: Retorna um resumo consolidado da carteira.
- **Resposta (Sucesso - 200 OK)**:
  ```json
  {
    "totalInvested": 65518.00,
    "totalByType": {
      "RENDA_FIXA": 25000.00,
      "CRIPTO": 35000.00,
      "FUNDO": 1600.00,
      "ACAO": 3718.00
    },
    "assetCount": 5
  }
  ```

---

### Critérios de Implementação

- **Clareza e Legibilidade**: O código foi escrito seguindo as convenções do Java e do Spring, com nomes de variáveis e métodos claros e comentários onde necessário.
- **Organização**: A estrutura do projeto em pacotes (`controller`, `service`, `repository`, `model`, `dto`) garante uma separação clara de responsabilidades.
- **Regras de Negócio**: A lógica de negócio, como cálculos de totais e agrupamentos, está encapsulada na camada de `Service`.
- **Simplicidade e Detalhes**: A solução é simples e direta, mas implementa todos os requisitos funcionais com atenção aos detalhes, como o uso de `BigDecimal` para valores monetários e o tratamento de exceções para casos como "ativo não encontrado".
