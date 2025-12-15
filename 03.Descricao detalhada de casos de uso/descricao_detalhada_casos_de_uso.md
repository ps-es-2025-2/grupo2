# Descrição Detalhada dos Casos de Uso do Sistema GEB

## Histórico de Revisão

| Data | Versão | Descrição | Autor |
| :--- | :--- | :--- | :--- |
| 29/11/2025 | 2.1 | Remoção do módulo de relatórios e ajuste de casos de uso. | Equipe de Desenvolvimento |
| 24/09/2025 | 2.0 | Detalhamento dos fluxos alternativos e pré-condições. | Ana Carolyne |
| 24/09/2025 | 1.0 | Versão inicial dos casos de uso. | Fernanda Marques |

## 1. UC01 – Autenticar Usuário

*Atores:* Administrador, Caixa, Conferente
*Objetivo:* Permitir que o usuário acesse o sistema mediante credenciais válidas.
*Pré-condições:* O usuário deve estar previamente cadastrado.

*Fluxo Principal:*
1. O usuário informa nome de usuário e senha.
2. O sistema valida as credenciais.
3. O sistema identifica o perfil (ADMIN, CAIXA, CONFERENTE).
4. O sistema libera o acesso conforme as permissões do perfil.

*Fluxos Alternativos:*
* 2a. Credenciais inválidas → O sistema exibe mensagem de erro e solicita nova tentativa.

*Pós-condições:* O usuário está autenticado e com sessão ativa.

---

## 2. UC02 – Gerenciar Evento

*Ator:* Administrador
*Objetivo:* Criar, editar e alterar o estado de um evento (PLANEJADO, PREPARACAO, EXECUCAO, PAUSADO, FINALIZADO).
*Pré-condições:* Administrador autenticado.

*Fluxo Principal:*
1. Administrador acessa "Gerenciar Evento".
2. Cria novo evento ou seleciona um existente.
3. Define nome, datas e estado inicial.
4. Salva as alterações.

*Fluxos Alternativos:*
* 3a. Tentativa de alterar para EXECUÇÃO sem estoque mínimo configurado → sistema bloqueia.

*Pós-condições:* Evento registrado e associado ao estoque.

---

## 3. UC03 – Gerenciar Estoque

*Ator:* Administrador
*Objetivo:* Cadastrar bebidas, configurar estoque inicial, ajustar quantidade mínima e visualizar níveis atuais.
*Pré-condições:*
* Evento existente.
* Administrador autenticado.

*Fluxo Principal:*
1. Administrativo seleciona um produto do estoque.
2. Informa quantidade inicial e mínima.
3. Salva as configurações.

*Pós-condições:* O estoque do evento é atualizado.

---

## 4. UC04 – Registrar Venda (PDV)

*Ator:* Caixa
*Objetivo:* Registrar venda de bebidas durante o evento e gerar ficha/QR Code.
*Pré-condições:*
* Caixa autenticado.
* Caixa aberto.
* Evento em EXECUÇÃO.

*Fluxo Principal:*
1. Caixa seleciona bebidas e quantidades.
2. O sistema calcula o total.
3. O caixa confirma a venda.
4. O sistema gera o registro Venda e seus ItensVenda.
5. O sistema reduz automaticamente o estoque.
6. O sistema registra movimentação de caixa (VENDA).
7. O sistema gera a FichaQRCode com código único.

*Fluxos Alternativos:*
* 2a. Estoque insuficiente → sistema alerta e impede finalização.

*Pós-condições:*
* Estoque atualizado.
* Venda registrada.
* Ficha QR Code gerada.

---

## 5. UC05 – Validar Ficha QR Code

*Ator:* Conferente
*Objetivo:* Garantir que cada ficha entregue seja válida e não reutilizada.
*Pré-condições:*
* Conferente autenticado.
* Venda existente associada ao QR Code.

*Fluxo Principal:*
1. Conferente escaneia ou digita o código da ficha.
2. O sistema verifica validade e autenticidade.
3. O sistema libera entrega dos produtos.

*Fluxos Alternativos:*
* 2a. Ficha já utilizada → Sistema recusa.
* 2b. Código inexistente → Sistema recusa.

*Pós-condições:* Ficha marcada como utilizada (se aplicável).

---

## 6. UC06 – Solicitar Reposição de Estoque

*Ator:* Caixa
*Objetivo:* Enviar solicitação de reposição quando estoque atingir nível crítico.
*Pré-condições:*
* Caixa autenticado.
* Estoque abaixo da quantidade mínima.

*Fluxo Principal:*
1. Caixa acessa solicitação de reposição.
2. Informa quantidade desejada.
3. Sistema registra SolicitacaoReposicao com status PENDENTE.

*Pós-condições:* Pedido pendente aguardando decisão do Administrador.

---

## 7. UC07 – Gerenciar Solicitações de Reposição

*Ator:* Administrador
*Objetivo:* Aprovar ou negar pedidos de reposição.
*Pré-condições:* Solicitação pendente.

*Fluxo Principal:*
1. Admin acessa lista de solicitações.
2. Seleciona uma solicitação.
3. Aprova ou nega.
4. Se aprovada, sistema aumenta o estoque.
5. Sistema atualiza status para APROVADA ou NEGADA.

*Pós-condições:* Estoque atualizado (caso aprovado).

---

## 8. UC08 – Abrir Caixa

*Ator:* Caixa
*Objetivo:* Iniciar o funcionamento do caixa no início do turno.
*Pré-condições:*
* Caixa autenticado.
* Não existir caixa aberto para o usuário.

*Fluxo Principal:*
1. Caixa informa saldo inicial.
2. Sistema registra abertura como MovimentacaoCaixa (ABERTURA).
3. Sistema cria caixa com status aberto.

*Pós-condições:* Caixa ativo para registrar vendas.

---

## 9. UC09 – Fechar Caixa

*Ator:* Caixa
*Objetivo:* Encerrar operações e registrar saldo final.
*Pré-condições:* Caixa aberto.

*Fluxo Principal:*
1. Caixa solicita fechamento.
2. Sistema calcula saldo final com base nas movimentações.
3. Sistema registra MovimentacaoCaixa (FECHAMENTO).
4. Sistema finaliza o caixa.

*Pós-condições:* Caixa encerrado com saldo registrado.
