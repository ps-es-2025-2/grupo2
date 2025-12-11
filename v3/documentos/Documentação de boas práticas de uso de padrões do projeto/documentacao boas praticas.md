# Documentação de Boas Práticas do Projeto

## 1. Arquitetura em Camadas

O projeto segue a estrutura **Controller → Service → Repository → Entity**.

**Exemplos no código:**
- VendaController, VendaService, VendaRepository, Venda
- UsuarioController, UsuarioService, UsuarioRepository, Usuario

**Boas práticas aplicadas:**
* Controllers apenas recebem requisições e chamam services.
* Services concentram a lógica de negócio.
* Repositórios somente acessam o banco.
* Uso de DTOs/Requests para entrada e saída de dados.

## 2. Repository Pattern

Implementado com Spring Data JPA, usando interfaces que estendem JpaRepository.

**Exemplos:**
- VendaRepository extends JpaRepository<Venda, Long>
- UsuarioRepository extends JpaRepository<Usuario, Long>

**Boas práticas:**
* Uso de métodos derivados (findByEmail, findByUsername, etc.).
* Queries customizadas somente quando necessário.

## 3. DTO Pattern

Uso de DTOs e classes Request/Response para não expor entidades diretamente.

**Exemplos:**
- UsuarioRequest, UsuarioResponse
- VendaRequest, VendaResponse

**Boas práticas:**
* Validação com @NotNull, @NotEmpty, etc.
* Conversão entre Entity ↔ DTO feita nos serviços.

## 4. Builder Pattern (Lombok)

Entidades usam @Builder para facilitar criação de objetos.

**Exemplo baseado no código real de Venda:**

```java
Venda venda = Venda.builder()
    .cliente(cliente)
    .dataHora(LocalDateTime.now())
    .valorTotal(valorTotal)
    .operador(operador)
    .itens(listaDeItens)
    .ficha(fichaDigital)
    .evento(evento)
    .validacoes(validacoes)
    .build();
```

## 5. Transações (@Transactional)

Serviços que alteram o estado usam @Transactional.

**Exemplos do projeto:**
* Métodos de salvar venda
* Processos de validação de ficha
* Lógica de movimentação de estoque

Consultas usam @Transactional(readOnly = true) quando necessário.

## 6. Injeção de Dependências

Feita pelo Spring, normalmente com Lombok.

**Exemplo:**

```java
@Service
@RequiredArgsConstructor
public class VendaService {
    private final VendaRepository vendaRepository;
}
```

**Boas práticas:**
* Uso de constructor injection.
* Classes anotadas com @Service, @Repository, @Component.

## 7. Exception Handling Centralizado

O projeto possui um @RestControllerAdvice.

**Exemplos:**
* Tratamento de MethodArgumentNotValidException retornando erros padronizados.
* Tratamento de ResourceNotFoundException.

## 8. Segurança (JWT)

Autenticação via JWT com filtro personalizado.

**Implementado no código:**
* JwtAuthenticationFilter
* JwtUtils
* TokenService
* SecurityConfig

**Observações:**
* Duas classes geram/validam tokens (JwtUtils e TokenService).
* A chave jwt.secret está exposta no application.properties.

---

## Recomendações de Melhoria

### Segurança
- [ ] Mover jwt.secret para variáveis de ambiente
- [ ] Consolidar JwtUtils e TokenService em uma única classe
- [ ] Implementar refresh tokens

### Código
- [ ] Adicionar testes unitários e de integração
- [ ] Implementar logging estruturado
- [ ] Adicionar documentação Swagger/OpenAPI
