# Elevator API — Spring Boot

## 📌 Sobre o projeto

API REST desenvolvida em Java com Spring Boot para simular o gerenciamento e controle de elevadores, aplicando boas práticas de arquitetura back-end, separação de responsabilidades, tratamento de erros e testes automatizados.

### A API é capaz de:

* Criar, buscar, listar e deletar elevadores
* Adicionar e remover passageiros com validação de capacidade
* Subir e descer andares com validação de limites
* Retornar erros padronizados para operações inválidas

---

## 🧠 Arquitetura

O projeto foi organizado em camadas, seguindo os princípios de uma arquitetura back-end profissional:

```
domain      → entidade Elevator com regras de negócio
repository  → contrato de persistência e implementações
service     → orquestração entre domínio e persistência
controller  → exposição dos endpoints HTTP
dto         → objetos de entrada e saída de dados
exception   → exceções customizadas por cenário
handler     → tratamento centralizado de erros
config      → configurações da aplicação (Swagger)
```

---

## ⚙️ Regras de negócio

As validações ficam concentradas no domínio, garantindo que o elevador nunca fique em um estado inválido:

* Subir além do último andar
* Descer abaixo do térreo
* Exceder a capacidade máxima de passageiros
* Remover passageiro quando o elevador está vazio
* Quando uma regra é violada, o domínio lança uma exceção customizada com o ID do elevador

---

## ⚠️ Tratamento de exceções

Todas as exceções são tratadas de forma centralizada pelo `GlobalExceptionHandler`, que retorna respostas padronizadas no seguinte formato:

```json
{
  "timestamp": "2026-03-22T10:00:00",
  "status": 400,
  "message": "Invalid operation for elevator 1, because it's already full",
  "path": "/elevators/1/add"
}
```

Cenários cobertos:
* Elevador não encontrado → `404 Not Found`
* Operação inválida (cheio, vazio, limite de andar) → `400 Bad Request`
* Campos inválidos na requisição → `400 Bad Request`
* JSON mal formatado → `400 Bad Request`

---

## 🔀 Endpoints

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/elevators` | Cria um elevador |
| `GET` | `/elevators` | Lista todos os elevadores |
| `GET` | `/elevators/{id}` | Busca elevador por ID |
| `DELETE` | `/elevators/{id}` | Deleta um elevador |
| `PATCH` | `/elevators/{id}/add` | Adiciona um passageiro |
| `PATCH` | `/elevators/{id}/remove` | Remove um passageiro |
| `PATCH` | `/elevators/{id}/up` | Sobe um andar |
| `PATCH` | `/elevators/{id}/down` | Desce um andar |

A documentação interativa completa está disponível via Swagger ao rodar a aplicação:
```
http://localhost:8080/swagger-ui/index.html
```

---

## 🗃️ Persistência e Profiles

O projeto utiliza dois repositórios ativados por Spring Profiles:

| Profile | Repositório | Uso |
|---------|-------------|-----|
| `dev` / `test` | `InMemoryRepository` | Desenvolvimento e testes |
| `prod` | `JpaElevatorRepository` | Produção com H2 |

---

## ✅ Testes

O projeto possui cobertura de testes em três camadas:

**Testes de domínio** — validam as regras de negócio isoladas, sem dependências externas.

**Testes de serviço** — validam a orquestração da service com o repositório mockado via Mockito.

**Testes de integração** — validam os endpoints HTTP de ponta a ponta com MockMvc, cobrindo cenários de sucesso e erro para todos os endpoints.

---

## 🛠️ Tecnologias

* Java 21
* Spring Boot 3.5.12
* Spring Data JPA
* H2 Database
* Bean Validation (Jakarta)
* JUnit 5
* Mockito
* SpringDoc OpenAPI (Swagger UI)
* Maven

---

## 🚀 Como executar

```bash
# Clone o repositório
git clone https://github.com/EuPhilipeCarvalho/ElevatorAPI.git

# Acesse a pasta do projeto
cd ElevatorAPI

# Execute com Maven
./mvnw spring-boot:run
```

A aplicação sobe na porta `8080` com o profile `prod` ativo por padrão.

Para executar os testes:
```bash
./mvnw test
```

---

## 👤 Autor

**Philipe Carvalho**
Estudante de Análise e Desenvolvimento de Sistemas
