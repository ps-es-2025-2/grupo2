# Visão do Produto: Gestor de Eventos de Bebidas (GEB)

## Histórico de Revisão

| Data | Versão | Descrição | Autor |
| :--- | :--- | :--- | :--- |
| 29/09/2025 | 2.2 | Reunião com o stakeholder para entendimento do problema e expectativas. | Ana Carolyne |
| 24/09/2025 | 2.1 | Adequação de requisitos básicos | Ana Carolyne |
| 24/09/2025 | 2.0 | Adequação ao template formal da UFLA. | Gemini 2.5 Pro |
| 24/09/2025 | 1.1 | Adição do escopo e orientação do entrevistado. | Luiz Felipe |
| 24/09/2025 | 1.0 | Versão inicial. | Fernanda Marques |

## 1. Introdução

Este documento tem por objetivo trazer clareza quanto à necessidade de desenvolver o sistema Gestor de Eventos de Bebidas (GEB). Ele disponibiliza detalhes sobre as características-chave necessárias para o produto, as partes interessadas, o problema a ser resolvido, as restrições e os riscos, servindo como um guia para o planejamento e execução do projeto.

### 1.1. Escopo e Alinhamento Estratégico

Este documento de visão abrange o desenvolvimento de um novo produto de software para gestão de vendas e estoque em eventos. O projeto está alinhado com a necessidade estratégica de organizadores de eventos de modernizar suas operações, aumentar a eficiência, reduzir perdas e utilizar dados para tomar decisões mais inteligentes.

### 1.2. Definições, Acrônimos e Abreviações

- **GEB:** Gestor de Eventos de Bebidas.
- **PDV:** Ponto de Venda.
- **Stakeholder:** Parte interessada no projeto.
- **UI:** User Interface (Interface do Usuário).
- **UX:** User Experience (Experiência do Usuário).

## 2. Análise de Contexto

### 2.1. Detalhamento da Necessidade

Desenvolver um sistema de Ponto de Venda (PDV) focado em ambientes festivos (quermesses), que otimize a venda de produtos através de um sistema de fichas digitais impressas. O software deverá fornecer controle total de estoque com alertas de nível mínimo, gestão de usuários com permissões específicas para venda e entrega, e funcionalidades que garantam agilidade no atendimento e segurança em todas as transações do evento.

#### 2.1.1. Descrição do Problema

O gerenciamento de bebidas em festas e eventos de pequeno e médio porte é predominantemente manual, baseado em anotações em papel ou planilhas eletrônicas. Esse método é propenso a falhas, como erros de contagem de estoque, perdas não contabilizadas (desvios ou desperdício), lentidão no atendimento ao cliente final e extrema dificuldade na apuração dos resultados financeiros (lucro, produtos mais vendidos, etc.) ao final do evento.

#### 2.1.2. Parte(s) afetada(s)

- **Organizadores do Evento:** São diretamente afetados pelo impacto financeiro das perdas e pela falta de dados para planejar eventos futuros.
- **Gerentes de Bar:** Sofrem com a dificuldade de controlar a equipe, gerenciar o estoque em tempo real e realizar o fechamento de caixa de forma precisa.
- **Equipe de Vendas (Caixas e Barmans):** Enfrentam a pressão de um atendimento lento e a complexidade de registrar vendas em sistemas manuais ou inadequados.
- **Clientes do Evento:** São indiretamente afetados por longas filas e possíveis erros nos pedidos.

#### 2.1.3. Impacto

O impacto principal é financeiro, com perdas de receita diretas. A lentidão na venda e validação manual das fichas gera longas filas, fazendo com que clientes desistam da compra. Adicionalmente, o impacto operacional é severo: o controle arcaico das fichas, que não possuem um código único para rastreabilidade, abre margem para erros de contagem, perdas e dificulta o fechamento de caixa. Essa confusão generalizada no balcão de troca prejudica a imagem da distribuidora de Marcilino, comprometendo sua reputação como fornecedora para futuros eventos.

#### 2.1.4. Solução de Sucesso

Uma solução de sucesso automatizará o registro de vendas, baixa de estoque e distribuição de fichas, fornecerá relatórios em tempo real sobre o desempenho das vendas e a situação do inventário, e simplificará o processo de abertura e fechamento de caixa para a equipe.

### 2.2. Alternativas

**Alternativa 1: Manter o Status Quo (Planilhas e Papel)**
- **Pontos Fortes:** Custo zero de implantação de software. Nenhuma necessidade de treinamento em novas tecnologias.
- **Pontos Fracos:** Alta propensão a erros, falta de dados em tempo real, insegurança, lentidão e ineficiência.

**Alternativa 2: Utilizar um Sistema de PDV Genérico**
- **Pontos Fortes:** Soluções prontas e robustas para vendas.
- **Pontos Fracos:** Geralmente são complexos, caros e não otimizados para a dinâmica de eventos (gestão de estoque por evento, múltiplas frentes de caixa temporárias, etc.).

## 3. Partes Interessadas

### 3.1. Partes Interessadas

| Unidade | Representada por | Envolvimento com o projeto |
| :--- | :--- | :--- |
| Equipe de Desenvolvimento | Brenna Amorim | Responsável pela construção e entrega do software. |
| Equipe de Desenvolvimento | Fernanda Marques de Abreu | Responsável por projetar a estrutura técnica do software |
| Equipe de Desenvolvimento | Luiz Felipe Belisário Macedo | Responsável por desenvolver as funcionalidades do sistema (Desenvolvedor Backend e Frontend) |
| Equipe de Desenvolvimento | Ana Carolyne Pereira de Souza | Responsável por garantir a qualidade do software |

### 3.2. Usuários

| Tipo do usuário | Representante(s) | Descrição | Responsabilidades |
| :--- | :--- | :--- | :--- |
| Administrador | Marcilino (dono da distribuidora) | Usuário com acesso total ao sistema, responsável por configurar o evento e analisar os resultados. | - Cadastrar produtos, preços e estoque inicial |
| Entregador / Barman | Funcionários do evento | Usuário responsável por validar a ficha e entregar o produto ao cliente. | - Realizar a baixa da ficha no sistema (via leitor de código ou inserção manual). <br> - Entregar o produto correspondente ao cliente. |

### 3.3. Necessidades das Partes Interessadas ou Usuários

#### 3.3.1. Controle de Estoque em Tempo Real

| | |
| :--- | :--- |
| **Parte(s) Interessada(s)** | "Organizador do Evento, Gerente de Bar" |
| **Motivadores** | "Problema: Não saber quais bebidas estão acabando durante o evento, levando à perda de vendas ou à necessidade de compras emergenciais. " |
| **Situação atual** | "O controle é feito visualmente ou por meio de contagens manuais periódicas, que são imprecisas e interrompem o fluxo de trabalho. " |
| **Solução ideal** | "Uma tela no sistema que mostre, em tempo real, a quantidade restante de cada produto, com alertas visuais para itens em nível crítico. " |

#### 3.3.2. Agilidade no Fechamento de Caixa

| | |
| :--- | :--- |
| **Parte(s) Interessada(s)** | "Gerente de Bar, Operador de Caixa" |
| **Motivadores** | Problema: O processo de fechamento de caixa ao final do turno é demorado e sujeito a erros de conciliação entre vendas registradas e dinheiro/pagamentos recebidos. |
| **Situação atual** | "Contagem manual de dinheiro e conferência com anotações de vendas, o que pode levar muito tempo e gerar conflitos. " |
| **Solução ideal** | "O sistema deve fornecer um relatório de fechamento de caixa com um clique, mostrando o total de vendas (separado por método de pagamento) que o operador deve ter em mãos. " |

### 3.4. Histórias de Usuário

Para clarificar as dores do cliente e guiar o desenvolvimento, as seguintes histórias de usuário foram levantadas:

- **Como** administrador do evento (Marcilino), **eu quero** ter um painel com o status do estoque em tempo real, **para que** eu possa tomar decisões rápidas sobre a reposição de produtos e evitar a perda de vendas.

- **Como** administrador do evento, **eu quero** gerar relatórios de vendas ao final do evento, **para que** eu possa entender quais produtos são mais vendidos e planejar melhor as compras futuras, otimizando meu lucro.

- **Como** um operador de caixa, **eu quero** registrar vendas de forma rápida e intuitiva, **para que** eu possa reduzir o tempo de espera nas filas e atender mais clientes.

- **Como** um gerente de bar, **eu quero** um sistema que facilite o fechamento de caixa de cada operador, **para que** o processo seja mais rápido, preciso e com menos chances de erro na contagem do dinheiro.

- **Como** um entregador/barman, **eu quero** validar as fichas dos clientes de maneira simples e segura (como a leitura de um código), **para que** eu possa entregar o produto correto rapidamente e evitar fraudes ou confusão no balcão de troca.

## 4. Objetivos de Negócio

| Objetivo de negócio | Descrição |
| :--- | :--- |
| Reduzir Perdas de Estoque | "Diminuir em pelo menos 15% as perdas não identificadas de produtos (desvios, desperdício) nos primeiros 3 meses de uso. " |
| Aumentar a Eficiência do Atendimento | "Reduzir o tempo médio por transação de venda em 25%, diminuindo filas e aumentando a satisfação do cliente. " |
| Otimizar a Compra de Insumos | "Utilizar os relatórios de ""produtos mais vendidos"" para otimizar em 20% a assertividade nas compras para eventos futuros. " |

## 5. Visão Geral do Produto

### 5.1. Perspectiva do Produto

O GEB é um sistema independente e autocontido, projetado para ser o núcleo operacional da gestão de bebidas em eventos. Ele substitui completamente os processos manuais e planilhas, funcionando como um PDV especializado com um forte componente de gestão de inventário em tempo real.

### 5.2. Características-Chave do Produto

| Característica-chave | Descrição | Prioridade |
| :--- | :--- | :--- |
| Módulo de Vendas (PDV) | "Interface ágil para registro de vendas de produtos, com suporte a diferentes métodos de pagamento. " | Essencial |
| Controle de Estoque | Baixa automática do inventário a cada venda e visualização do estoque em tempo real. | Essencial |
| Painel de Relatórios | "Geração de relatórios de desempenho: vendas totais, produtos mais vendidos, lucratividade e estoque final. " | Alta |
| Gestão de Caixa | "Funcionalidades para abertura, fechamento e sangria de caixa por operador. " | Alta |
| Gestão Multi-evento | Capacidade de cadastrar diferentes eventos para isolar os dados de vendas e estoque de cada um. | Média |

#### 5.2.1. Requisitos não Funcionais

| Tipo | Descrição | Prioridade |
| :--- | :--- | :--- |
| Usabilidade | "A interface do PDV deve ser extremamente intuitiva, exigindo menos de 10 minutos de treinamento para um novo operador de caixa. " | Essencial |
| Desempenho | "O sistema deve ser capaz de processar uma transação de venda em menos de 2 segundos, mesmo sob alta carga de uso. " | Alta |
| Disponibilidade | O sistema deve ter uma disponibilidade de 99.9% durante o período de um evento. | Alta |
| Segurança | "Os dados de vendas e acesso de usuários devem ser protegidos, em conformidade com a Lei Geral de Proteção de Dados (LGPD), se aplicável. " | Média |

### 5.3. Suposições e Dependências

- **Suposição:** Assume-se que os locais dos eventos terão acesso a uma rede de internet estável para o funcionamento do sistema.
- **Dependência:** O projeto depende da disponibilidade dos stakeholders (Organizador, Gerente de Bar) para sessões de levantamento de requisitos e validação.

### 5.7. Restrições

| Tipo | Nome | Descrição |
| :--- | :--- | :--- |
| Tempo | Prazo do Projeto | O MVP (Produto Mínimo Viável) deve ser entregue em 4 meses. |
| Financeira | Orçamento do Projeto | O projeto não deve exceder o custo total de <valor do orçamento>. |
| Tecnológica | Plataforma Web | "A solução deve ser desenvolvida como uma aplicação web responsiva, sem a necessidade de instalação de software nativo. " |

### 5.8. Riscos

| Nome do Risco | Tipo do Risco | Probabilidade | Impacto |
| :--- | :--- | :--- | :--- |
| Falha de Conexão no Evento | Operacional | Média | Alto |
| Baixa Adoção pela Equipe | Operacional | Baixa | Médio |
| Escopo mal Definido (Scope Creep) | Gerencial | Média | Alto |
| Segurança de Dados | Técnico | Baixa | Alto |
