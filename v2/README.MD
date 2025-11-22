# GEB — Gestor de Eventos de Bebidas

## 1. Introdução

O **Gestor de Eventos de Bebidas (GEB)** é um sistema web projetado para otimizar o controle de vendas, estoque e caixa em eventos temporários, como quermesses, festas comunitárias, eventos beneficentes e festivais.

O sistema substitui o modelo manual tradicional por uma solução digital prática, segura e organizada, permitindo maior eficiência operacional e mais precisão na apuração dos resultados.

## 2. Contexto e Problema

A gestão manual de bebidas em eventos geralmente envolve anotações em papel, planilhas simples ou fichas físicas. Esses métodos apresentam problemas recorrentes, incluindo:

- Erros de contagem e falhas humanas no registro das vendas.
- Falta de rastreabilidade das operações.
- Controle ineficiente do estoque, dificultando reposições durante o evento.
- Baixa transparência financeira.
- Filas longas devido à lentidão no registro de compras.
- Dificuldade em identificar produtos mais vendidos ou horários de pico.

Esses desafios comprometem tanto a experiência dos visitantes quanto a lucratividade do evento.

## 3. Proposta da Solução

O **GEB** propõe uma solução digital integrada formada pelos seguintes pilares:

- Ponto de Venda (PDV) moderno e rápido.
- Geração de fichas digitais com código único.
- Controle de estoque totalmente automatizado.
- Gestão completa de caixa (abertura, fechamento e sangria).
- Painel administrativo para cadastro, consulta e análise.
- Relatórios gerenciais para avaliação pós-evento.

## 4. Objetivos do Sistema

### Objetivo Geral
Automatizar e padronizar a gestão de vendas e estoque de bebidas em eventos, reduzindo erros e aumentando a eficiência operacional.

### Objetivos Específicos
- Garantir registro confiável de todas as vendas.
- Evitar desperdícios e desvios com controle em tempo real.
- Facilitar a conferência financeira ao final do evento.
- Fornecer uma interface simples e intuitiva para operadores.
- Permitir que administradores configurem produtos e estoques com autonomia.

## 5. Funcionalidades Detalhadas

### 5.1 Módulo de Vendas (PDV)
- Seleção rápida de produtos.
- Cálculo automático do total.
- Geração de ficha digital identificada por código único.
- Layout responsivo para tablets e dispositivos móveis.

### 5.2 Controle de Estoque
- Redução automática do estoque a cada venda registrada.
- Atualização em tempo real refletida para administradores.
- Alerta de estoque mínimo configurável.
- Registro de movimentações para auditoria.

### 5.3 Gestão de Caixa
- Abertura de caixa com valor inicial.
- Registro de vendas por operador.
- Registro de sangrias quando necessário.
- Fechamento automático e emissão de resumo financeiro.

### 5.4 Validação de Fichas
- Leitura do código da ficha (ID ou QR Code).
- Verificação de autenticidade.
- Redução do estoque de entrega.
- Prevenção de reutilização de fichas.

### 5.5 Administração
- Cadastro de produtos, preços e categorias.
- Definição do estoque inicial para o evento.
- Cadastro e gerenciamento de usuários.
- Acesso a relatórios completos.

### 5.6 Relatórios e Métricas
- Vendas totais por produto.
- Quantidade vendida por horário.
- Painel de desempenho dos operadores.
- Resumo final do evento (financeiro e de estoque).

## 6. Usuários e Perfis

### Administrador
Responsável pela configuração geral do evento.
Permissões incluem:
- Gerenciar produtos.
- Gerenciar estoque.
- Consultar relatórios.
- Criar e editar usuários.
- Acessar ajustes avançados.

### Funcionário (Operador)
Responsável pelo atendimento e entrega.
Permissões incluem:
- Registrar vendas no PDV.
- Emitir fichas.
- Validar fichas.
- Registrar sangrias (opcional).

## 7. Arquitetura do Sistema
*(Esta seção pode ser adaptada conforme o stack que vocês escolherem.)*

### Componentes (exemplo)
- **Frontend:** React, Next.js ou Vue.js.
- **Backend:** Node.js (Express), NestJS, Laravel ou Django.
- **Banco de Dados:** PostgreSQL ou MySQL.
- **Autenticação:** JWT.
- **Implantação:** Docker, Render, Railway ou VPS.
- **Impressão de fichas:** API local ou integração com impressoras térmicas.

### Fluxo Geral
1. O operador registra a venda no PDV.
2. O sistema gera uma ficha digital com código único.
3. A venda é registrada e o estoque é atualizado.
4. O cliente apresenta a ficha na entrega.
5. O funcionário valida a ficha e libera o produto.
6. Administradores acessam relatórios durante e após o evento.