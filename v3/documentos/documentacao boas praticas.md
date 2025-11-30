# Documentação de Boas Práticas de Uso de Padrões de Projeto no Sistema

Esta documentação apresenta as boas práticas e os padrões de projeto recomendados para a arquitetura do sistema, considerando o uso de **Spring Boot**, **PostgreSQL**, **Docker**, **Maven** na API e **HTML/CSS/JavaScript** no front-end seguindo **MVC**.  
Os padrões aqui descritos visam garantir **clareza de código**, **extensibilidade**, **baixo acoplamento**, **alta coesão** e **facilidade de manutenção**.

---

# 1. Arquitetura Geral

A arquitetura do sistema é dividida em camadas:

### • Camada de Apresentação (Front-End)
Interface desenvolvida em **HTML**, **CSS** e **JavaScript**, seguindo o padrão **MVC** do lado do cliente.

### • Camada de API (Back-End)
Desenvolvida em **Spring Boot**, organizada por **controllers**, **services** e **repositories**. Comunicação **RESTful**.

### • Camada de Persistência
Banco **PostgreSQL** executando em **container Docker**.

### • Camada de Build e Gestão
**Maven** para dependências e empacotamento.

Os padrões de projeto descritos se aplicam principalmente ao lado do back-end e aos fluxos gerais do sistema.

---

# 2. Padrões de Projeto Aplicados

## 2.1 MVC (Model–View–Controller)

### **Front-end**
- **Model:** estruturas de dados JS e responses da API  
- **View:** HTML + CSS  
- **Controller:** scripts JS que manipulam eventos, chamam a API e atualizam a interface  

### **Back-end**
- **Model:** entidades JPA (Produto, Estoque, Venda, ItemVenda, Usuario etc.)  
- **Controller:** endpoints REST  
- **Service:** lógica de negócio  
- **Repository:** acesso ao banco  

**Boas práticas:**
- Nunca colocar lógica de negócio no controller  
- Nunca acessar o banco direto no controller  
- Garantir validações no serviço e no domínio  

---

## 2.2 Repository (Spring Data JPA)

Cada agregado possui seu próprio repositório (ProdutoRepository, VendaRepository etc).

**Boas práticas:**
- Usar repositories apenas para CRUD e queries específicas  
- Evitar lógica de negócio dentro dos repositórios  
- Usar *interfaces*, não implementações manuais  

---

## 2.3 Service Layer (padrão Service)

Isola a lógica de negócio em classes de serviço.

**Exemplos:**
- `VendaService` registra venda, valida estoque, gera ficha  
- `EventoService` gerencia estados do evento  

**Boas práticas:**
- Toda regra de negócio deve estar nos services ou nas entidades  
- Controllers devem apenas orquestrar  
- Evitar dependência circular entre serviços  

---

## 2.4 DTO (Data Transfer Object)

Usado para padronizar entrada e saída da API.

**Benefícios:**
- Segurança  
- Controle sobre informação enviada ao cliente  
- Evita acoplamento do front-end com entidades  

**Boas práticas:**
- Criar DTOs para criar, atualizar e visualizar  
- Converter Entidade ↔ DTO no service ou via mapper  

---

## 2.5 Dependency Injection (DI)

Baseado em Spring.

**Boas práticas:**
- Nunca usar `new` para classes gerenciadas pelo Spring  
- Sempre usar **injeção via construtor**  
- Evitar `@Autowired` em atributos  

---

## 2.6 Factory (Opcional)

Para criação de objetos complexos como Venda, ItemVenda, Ficha, Usuario etc.

**Boas práticas:**
- Usar quando a criação exigir regras  
- Evitar uso se não houver necessidade real  

---

## 2.7 Strategy (pagamentos, descontos, cálculos)

Recomendado quando houver diferentes regras intercambiáveis.

**Exemplos:**
- Dinheiro  
- Pix  
- Pagamento online  
- Políticas de desconto  

**Boas práticas:**
- Interface `PagamentoStrategy`  
- Uma implementação por tipo  
- Service decide qual estratégia usar  

---

## 2.8 Observer / Event Publisher (Spring Events)

Útil para acoplamento leve.

**Exemplos:**
- Registrar venda → atualizar estoque  
- Estoque baixo → notificar administrador  

**Boas práticas:**
- Usar apenas quando necessário  
- Evitar cadeias longas de eventos  

---

## 2.9 Singleton (Gerenciado pelo Spring)

Todos os services são singletons por padrão.

**Boas práticas:**
- Não criar singletons manuais  
- Confiar no container do Spring  

---

# 3. Boas Práticas Gerais

- Toda lógica crítica deve ter testes unitários  
- Nunca confiar no front-end para validações  
- Controllers devem ser finos; services, robustos  
- Campos obrigatórios devem ser validados  
- Regras de negócio ficam no domínio ou no service  
- Relacionamentos JPA bem definidos  
- Evitar métodos longos com `@Transactional`  
- Nunca expor entidades diretamente na API  
- Evitar regras de negócio nos repositórios  

---

# 4. Padrões Aplicados ao Seu Sistema Específico

Com base no sistema (evento, caixa, vendas, conferente, administrador), aplicam-se:

- **MVC** — organização das telas e da API  
- **Service** — VendaService, EstoqueService, EventoService  
- **Repository** — ProdutoRepository, VendaRepository  
- **DTO** — VendaDTO, ItemDTO, EventoDTO  
- **Strategy (opcional)** — formas de pagamento  
- **Factory (opcional)** — criação de fichas, vendas  
- **Observer** — notificações de estoque baixo  
- **Singleton** — serviços Spring  

Infraestrutura complementa:

- Controllers REST separados  
- Front-end independente  
- Docker padronizando execução  
- PostgreSQL isolado em container  

---

# 5. Recomendações Finais

- Domínio limpo: Evento, Caixa, Venda, ItemVenda, Produto, Usuario  
- Services coordenando o fluxo:  
  *venda → valida estoque → registra → atualiza → gera ficha → evento*  
- Utilizar DTOs para toda exposição de dados  
- Criar diagramas (classes, sequência, estados e BPM) para clareza  
- Regras como:  
  - estoque mínimo  
  - encerramento de evento  
  - fechamento de caixa  
  - validação de ficha  
  Devem ficar sempre nos **services**, nunca nos controllers.

---
