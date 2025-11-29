# Visão do Produto: Gestor de Eventos de Bebidas (GEB)

## Histórico de Revisão

| Data | Versão | Descrição | Autor |
| :--- | :--- | :--- | :--- |
| 24/09/2025 | 2.2 | Ajuste conforme Ata de Reunião Inicial e Casos de Uso detalhados. | Equipe de Desenvolvimento |
| 24/09/2025 | 2.1 | Adequação de requisitos básicos. | Ana Carolyne |
| 24/09/2025 | 2.0 | Adequação ao template formal da UFLA. | Gemini 2.5 Pro |
| 24/09/2025 | 1.0 | Versão inicial. | Fernanda Marques |

## 1. Introdução

Este documento tem por objetivo trazer clareza quanto à necessidade de desenvolver o sistema Gestor de Eventos de Bebidas (GEB). Ele disponibiliza detalhes sobre as características-chave necessárias para o produto, as partes interessadas, o problema a ser resolvido, as restrições e os riscos, servindo como um guia para o planejamento e execução do projeto.

### 1.1. Escopo e Alinhamento Estratégico

Este documento de visão abrange o desenvolvimento de um novo produto de software para gestão de vendas e estoque em eventos. O projeto está alinhado com a necessidade estratégica de organizadores de eventos de modernizar suas operações, aumentar a eficiência, reduzir perdas e utilizar dados para tomar decisões mais inteligentes.

### 1.2. Definições, Acrônimos e Abreviações

- **GEB:** Gestor de Eventos de Bebidas.
- **PDV:** Ponto de Venda.
- **Stakeholder:** Parte interessada no projeto.
- **QR Code:** Código de resposta rápida para validação de fichas.
- **UI:** User Interface (Interface do Usuário).
- **UX:** User Experience (Experiência do Usuário).

## 2. Análise de Contexto

### 2.1. Detalhamento da Necessidade

Desenvolver um sistema de Ponto de Venda (PDV) focado em ambientes festivos (quermesses), que otimize a venda de produtos através de um sistema de **fichas impressas com código único (QR Code)**. O software deverá fornecer controle total de estoque com alertas de nível mínimo, gestão de usuários com permissões específicas para venda (Caixa) e entrega (Conferente), e funcionalidades que garantam agilidade no atendimento e segurança em todas as transações do evento.

#### 2.1.1. Descrição do Problema

O Sr. Marcilino, proprietário de uma distribuidora em Vicentinópolis-GO, relatou perdas significativas devido ao uso de um sistema "arcaico" e manual. O método atual é desorganizado, sem rastreabilidade de fichas, gerando filas, erros de contagem e falta de controle sobre devoluções.

#### 2.1.2. Parte(s) afetada(s)

- **Organizadores do Evento:** Afetados pelo impacto financeiro das perdas e falta de dados.
- **Gerentes de Bar/Estoque:** Dificuldade em controlar estoque mínimo e reposição.
- **Equipe de Vendas (Caixas):** Pressão por atendimento lento e sistema manual.
- **Equipe de Entrega (Conferentes/Barmen):** Dificuldade em validar se uma ficha é verdadeira ou se já foi entregue.
- **Clientes do Evento:** Afetados por longas filas e confusão na retirada.

#### 2.1.3. Impacto

O impacto principal é financeiro e de reputação. A falta de códigos únicos nas fichas permite erros de contagem e possíveis fraudes. A confusão no balcão de troca prejudica a imagem da distribuidora, comprometendo sua contratação para futuros eventos.

#### 2.1.4. Solução de Sucesso

Uma solução de sucesso automatizará a geração de fichas com **QR Code único** no momento da venda. O sistema permitirá que o **Conferente** valide essa ficha via leitura digital para dar baixa no estoque e entregar o produto, eliminando a reutilização de fichas e garantindo a contagem correta.

### 2.2. Alternativas

**Alternativa 1: Manter o Status Quo (Planilhas e Papel)**
- **Pontos Fortes:** Custo zero de implantação.
- **Pontos Fracos:** Alta propensão a erros, falta de rastreabilidade, insegurança e lentidão.

**Alternativa 2: Utilizar um Sistema de PDV Genérico**
- **Pontos Fortes:** Soluções prontas.
- **Pontos Fracos:** Não possuem a lógica específica de "Venda -> Emissão de Ficha -> Validação por Conferente" necessária para a dinâmica de quermesses.

## 3. Partes Interessadas

### 3.1. Partes Interessadas

| Unidade | Representada por | Envolvimento com o projeto |
| :--- | :--- | :--- |
| Equipe de Desenvolvimento | Fernanda Marques de Abreu | Redatora da Ata e Desenvolvimento |
| Equipe de Desenvolvimento | Ana Carolyne Pereira de Souza | Equipe de Desenvolvimento |
| Cliente | Marcilino | Dono da Distribuidora e Validador dos Requisitos |

### 3.2. Usuários

| Tipo do usuário | Representante(s) | Descrição | Responsabilidades |
| :--- | :--- | :--- | :--- |
| Administrador | Marcilino (dono da distribuidora) | Usuário com acesso total. | - Configurar eventos, produtos e definir estoque mínimo. |
| Caixa | Funcionários do evento | Usuário responsável pela venda. | - Registrar vendas e emitir fichas com QR Code. |
| Conferente (Barman) | Funcionários do evento | Usuário responsável pela entrega. | - Validar a ficha (ler QR Code) e entregar o produto. |

### 3.3. Necessidades das Partes Interessadas ou Usuários

#### 3.3.1. Rastreabilidade e Segurança

| | |
| :--- | :--- |
| **Parte(s) Interessada(s)** | "Administrador, Conferente" |
| **Motivadores** | "Problema: Fichas manuais podem ser falsificadas, reutilizadas ou contadas erradas." |
| **Situação atual** | "Controle visual de fichas de papel sem identificação única." |
| **Solução ideal** | "Emissão de ficha com código único (ex: CERV-001) que é invalidado no sistema assim que o produto é entregue." |

#### 3.3.2. Controle de Estoque com Alertas

| | |
| :--- | :--- |
| **Parte(s) Interessada(s)** | "Administrador" |
| **Motivadores** | "Problema: Perda de vendas por falta de produto." |
| **Situação atual** | "Contagem manual e imprecisa." |
| **Solução ideal** | "Definição de estoque mínimo configurável com alertas visuais para reposição." |

### 3.4. Histórias de Usuário

- **Como** administrador (Marcilino), **eu quero** definir um estoque mínimo para cada produto, **para que** o sistema me avise quando for hora de repor as bebidas.

- **Como** operador de caixa, **eu quero** que o sistema imprima uma ficha com QR Code automaticamente após a venda, **para que** eu entregue ao cliente sem precisar preencher nada à mão.

- **Como** conferente (barman), **eu quero** escanear o código da ficha do cliente, **para que** o sistema confirme se é válida e dê baixa no estoque automaticamente, evitando erros.

## 4. Objetivos de Negócio

| Objetivo de negócio | Descrição |
| :--- | :--- |
| Eliminar Erros de Contagem | "Garantir 100% de rastreabilidade entre fichas vendidas e produtos entregues através do código único." |
| Agilizar o Atendimento | "Reduzir o tempo de fila utilizando validação rápida via leitura de código." |
| Reduzir Perdas | "Evitar prejuízos com fichas não contabilizadas ou extraviadas." |

## 5. Visão Geral do Produto

### 5.1. Perspectiva do Produto

O GEB é um sistema web independente, focado na dinâmica de eventos festivos. Ele integra o fluxo de venda (Caixa) com o fluxo de entrega (Conferente) através de identificadores únicos.

### 5.2. Características-Chave do Produto

| Característica-chave | Descrição | Prioridade |
| :--- | :--- | :--- |
| Módulo de Vendas (PDV) | Interface para registro de vendas e emissão de Ficha/QR Code. | Essencial |
| Validação de Fichas | Módulo para o Conferente ler o código e confirmar a entrega. | Essencial |
| Controle de Estoque | Baixa automática, cadastro de unidades (fardos vs unidade) e alertas de mínimo. | Essencial |
| Gestão de Caixa | Abertura e fechamento com registro de movimentações. | Alta |

### 5.3. Restrições e Riscos

| Tipo | Descrição |
| :--- | :--- |
| Restrição Tecnológica | Necessidade de impressora para as fichas e leitor de código (ou câmera) para o conferente. |
| Risco | Falha na leitura dos códigos QR devido à iluminação ou qualidade da impressão. |
