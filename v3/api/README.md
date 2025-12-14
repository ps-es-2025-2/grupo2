# GEB API - Gestão de Estoque e Bar

API desenvolvida para gerenciamento de estoque, eventos e vendas (fichas), com suporte a múltiplos estoques (Depósito Central e Eventos Específicos).

## 🚀 Tecnologias Utilizadas

O projeto utiliza a seguinte stack tecnológica:

* **Linguagem:** Java 21 (Amazon Corretto)
* **Framework:** Spring Boot 3
* **IDE Recomendada:** IntelliJ IDEA
* **Gerenciador de Dependências:** Maven
* **Banco de Dados:** PostgreSQL
* **Containerização:** Docker & Docker Compose
* **Versionamento de Banco:** Flyway

---

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado em sua máquina:

1.  **JDK 21 (Amazon Corretto)**
    * [Download aqui](https://docs.aws.amazon.com/corretto/latest/corretto-21-ug/downloads-list.html).
    * Verifique a instalação: `java -version`
2.  **Docker Desktop** (ou Engine + Compose)
    * Necessário para rodar o banco de dados.
3.  **IntelliJ IDEA** (Community ou Ultimate)
4.  **Git**

---

## ⚙️ Configuração do Ambiente

### 1. Clonar o Repositório

```bash
git clone [https://github.com/ps-es-2025-2/grupo2.git](https://github.com/ps-es-2025-2/grupo2.git)
````

### 2\. Configurar o SDK no IntelliJ

1.  Abra o projeto no IntelliJ.
2.  Vá em **File \> Project Structure \> Project**.
3.  No campo **SDK**, selecione `Add SDK > Download JDK`.
4.  Escolha **Version: 21** e **Vendor: Amazon Corretto**.
5.  Clique em Apply/OK e aguarde a indexação do Maven.

### 3\. Subir o Banco de Dados (Docker)

O projeto possui um arquivo `docker-compose.yml` na raiz para facilitar a inicialização do PostgreSQL.

No terminal (na raiz do projeto), execute:

```bash
docker-compose up -d
```

> **Nota:** Isso subirá um container PostgreSQL na porta `5432` com o banco `geb`.

### 4\. Configuração da Aplicação

Verifique o arquivo `src/main/resources/application.properties` (ou `.yml`). Certifique-se de que as credenciais batem com o Docker:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/geb
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=validate
```

-----

## ▶️ Como Rodar a Aplicação

### Via IntelliJ (Recomendado)

1.  Localize a classe principal `GebApiApplication.java` (geralmente em `src/main/java/br/com/geb/api`).
2.  Clique no ícone de "Play" (verde) ao lado do método `main`.

### Via Terminal (Maven)

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

-----

## 🛠️ Migrations (Banco de Dados)

Utilizamos o **Flyway** para versionamento do banco.

  * Ao rodar a aplicação pela primeira vez, as tabelas serão criadas automaticamente baseadas nos scripts em `src/main/resources/db/migration`.
  * **Atenção:** Não altere scripts de migração (`V00X__...`) que já foram executados. Se precisar mudar o banco, crie uma nova versão (ex: `V006__...`).

-----

## 🤝 Contribuindo

1.  Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`).
2.  Faça o commit (`git commit -m 'Adiciona nova funcionalidade'`).
3.  Faça o push (`git push origin feature/nova-funcionalidade`).
4.  Abra um Pull Request.

-----

**Desenvolvido por Equipe GEB**

````

***

### Dica Adicional: O `docker-compose.yml`
Se você ainda não tem o arquivo `docker-compose.yml` na raiz do projeto para acompanhar esse README, aqui está o código para você criar um agora:

```yaml
services:
  postgres:
    image: postgres:latest
    container_name: geb_postgres
    environment:
      POSTGRES_DB: geb
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data_geb:/var/lib/postgresql

volumes:
  postgres_data_geb:
````