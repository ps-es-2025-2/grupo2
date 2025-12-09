# Documentacao de boas praticas de uso de padroes do projeto

## Objetivo
Guia rapido para manter consistencia na API (Spring Boot/PostgreSQL) do projeto `br.com.geb.api`, cobrindo convencoes de codigo, arquitetura e cuidados operacionais.

## Estrutura de camadas
- `controller`: recebe HTTP, valida entrada basica, chama servicos.
- `service`: contem regras de negocio, orquestra repositorios, valida invariantes.
- `repository`: acesso a dados JPA (interfaces Spring Data).
- `domain`: entidades de dominio (JPA) e agregados.
- `dto`: contratos de entrada/saida (request/response). Evite expor entidades direto.
- `config`: configuracoes transversais (seguranca, CORS, JWT, init).

## Papeis principais
- Controller: apenas coordenar; nao colocar regra de negocio. Use `@Valid` nos DTOs.
- Service: validar negocio, checar existencia, consistencia, permissao basica; transacoes via `@Transactional` onde necessario.
- Repository: interfaces estendendo `JpaRepository`. Nomes de metodos descritivos; preferir queries derivadas antes de `@Query` custom.
- DTO + Mapper: DTO para entrada e saida; converta via metodos estaticos utilitarios ou pequenos mapeadores dedicados. Nao usar entidades como payload.

## Entidades JPA
- Sempre anotar com `@Entity` e `@Table`; chaves com `@Id` + `@GeneratedValue(strategy = GenerationType.IDENTITY)` (PostgreSQL).
- Relacionamentos: definir dono correto, `fetch` adequado (LAZY por padrao em *ToMany*). Configurar `cascade` apenas quando fizer sentido.
- Equals/HashCode: use apenas identificadores imutaveis (id) para evitar efeitos colaterais em colecoes.
- Campos obrigatorios: marcar com `@Column(nullable = false)` e validar tambem via DTO (`@NotNull`, `@NotBlank`, etc.).

## Transacoes
- Metodos de escrita em servicos marcados com `@Transactional`. Para leitura pesada, opcional `@Transactional(readOnly = true)`.
- Nao abra transacao no controller.

## Validacao
- DTOs com Bean Validation (`@NotNull`, `@NotBlank`, `@Size`, `@Email`, etc.).
- Use `@Valid` nos parametros de controller.
- Consistencia de negocio: validar dentro do service (ex.: estoque suficiente, caixa aberto, etc.).

## Seguranca e JWT
- Configuracao central em `config.SecurityConfig` e `JwtAuthenticationFilter`.
- Chave e expiracao via `application.properties` (`jwt.secret`, `jwt.expiration-ms`). Nunca comitar segredos reais.
- Proteja endpoints sensiveis por papel (`Papel` enum). Libere apenas o necessario em endpoints publicos (`/auth/**`).

## Tratamento de erros
- Padronizar respostas de erro (ex.: objeto JSON com `timestamp`, `status`, `error`, `message`, `path`).
- Criar `@ControllerAdvice`/`@ExceptionHandler` para excepcoes de negocio e validacao. Retornar 400 para erros de entrada, 404 para nao encontrado, 401/403 para autenticacao/autorizacao.
- Nao vazar stacktrace em producao.

## Logging
- Use `Slf4j` (Lombok) ou `LoggerFactory`. Mensagens curtas, contextualizadas. Nao logar dados sensiveis (senhas, tokens).
- Em erros, logar `error("msg", ex)` no service ou camada mais proxima da causa.

## Paginação e filtros
- Para listas, usar `Pageable`/`Page` do Spring Data. Validar limites de pagina para evitar abusos.
- Evitar retornar colecoes enormes; sempre permitir filtros por campos relevantes.

## DTOs de resposta
- Retornar apenas o necessario para o cliente. Para operacoes de escrita, retornar recurso criado/atualizado ou um resumo.
- Padronizar nomes de campos em snake_case ou camelCase (manter coerencia atual da API).

## Conversao entre DTO e entidade
- Criar metodos dedicados (ex.: `toEntity`, `fromEntity`) ou mappers simples. Evitar logica de negocio no mapper.

## Scripts e dados
- `application.properties` esta configurado para PostgreSQL local (host `localhost`, porta `5432`). Ajustar para ambientes com variaveis de ambiente.
- `spring.sql.init.mode=never` porque ha inicializacao via `DataInitializer`. Se mudar abordagem, manter consistente (usar `schema.sql`/`data.sql` ou migracoes como Flyway/Liquibase).

## Devtools e builds
- `spring-boot-devtools` esta em escopo runtime/optional; usar apenas em dev.
- Para rodar: `./mvnw clean package -DskipTests` e depois `java -jar target/geb-0.0.1-SNAPSHOT.jar`.
- Para `spring-boot:run`, use `./mvnw spring-boot:run -Dspring-boot.run.mainClass=br.com.geb.api.ApiApplication`.

## Nomenclatura e organizacao
- Classes: PascalCase; metodos/variaveis: camelCase; constantes: UPPER_SNAKE_CASE.
- Endpoints: substantivos no plural (`/produtos`, `/vendas`); verbos apenas para acoes especiais (`/caixas/{id}/fechar`).
- Pacotes: seguir estrutura atual (`controller`, `service`, `repository`, `domain`, `dto`, `config`).

## Testes
- Testes unitarios de regra de negocio no service. Mockar repositorios quando apropriado.
- Testes de integracao com `@SpringBootTest` e banco embarcado ou container test (se configurado).

## Performance e consultas
- Usar `@EntityGraph` ou DTO projections para evitar N+1 quando necessario.
- Cautela com `@ManyToMany`; preferir entidade de juncao (tabela associativa) explicita.

## Checklist rapido por feature
- [ ] DTO de entrada com validacao.
- [ ] Controller enxuto, usando `@Valid`.
- [ ] Service com regras e `@Transactional` quando escrever.
- [ ] Repositorio com metodos derivados claros.
- [ ] Tratamento de erro padronizado e retornos HTTP corretos.
- [ ] Teste minimo (unit ou integracao) cobrindo caso feliz e falhas relevantes.
- [ ] Logs adequados sem dados sensiveis.

## Boas praticas para frontend
- Arquitetura: seguir padrao de componentes funcionais e separar responsabilidades (UI pura, composicao, paginas). Evitar colocar regra de negocio na view.
- Estado: preferir estado local para UI e dados efemeros; estado global apenas para autenticacao, usuario e configuracoes. Para chamadas remotas, use hooks de dados (ex.: React Query/Axios + cache) com chaves bem definidas.
- Comunicacao com API: centralizar cliente HTTP e interceptors (auth, refresh de token). Padronizar tratamento de erros e mensagens ao usuario. Sempre lidar com estados de carregamento e erro na UI.
- Design system: manter biblioteca de componentes reutilizaveis (botao, input, modal, tabela). Documentar variacoes e tokens (cores, tipografia, espacamento). Evitar estilos ad-hoc.
- Acessibilidade: usar semantica HTML adequada, `aria-*` onde necessario, contraste suficiente, foco visivel e navegacao por teclado.
- Performance: evitar re-render desnecessario (memoizacao quando fizer sentido), carregamento sob demanda (code splitting) em paginas pesadas, debouncing em buscas.
- Segurança: nunca armazenar token em localStorage se for JWT sensivel sem mitigacao; preferir cookies httpOnly + CSRF ou, se usar bearer em memoria, proteger contra XSS. Sanitizar dados exibidos.
- Testes: testes de componentes para fluxos criticos (auth, formulários, carrinho/checkout). Usar testes de integração de UI para rotas principais e mocks de API.
- Observabilidade: logar erros de frontend (Sentry similar) e monitorar falhas de rede. Exibir mensagens amigaveis ao usuario.
