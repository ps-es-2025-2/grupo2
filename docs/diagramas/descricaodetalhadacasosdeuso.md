# Diagrama Global de Casos de Uso

## 1. Objetivo
O diagrama global de casos de uso tem como objetivo apresentar uma visão geral das interações entre os usuários (atores) e o sistema de vendas de fichas em eventos.  
Ele mostra **quem usa o sistema** e **quais funcionalidades principais estão disponíveis**, sem entrar no fluxo interno de cada caso.

---

## 2. Atores
- **Caixa**: principal usuário do sistema, responsável pelo registro de vendas, emissão de fichas, devoluções e controle de estoque.  
- **Administrador**: responsável por definir estoque mínimo, cadastrar produtos e analisar relatórios.  
- **Cliente**: compra fichas e as utiliza para retirar bebidas.  
- **Entregador (Barman)**: valida fichas no balcão e entrega o produto ao cliente.  

---

## 3. Casos de Uso
- **Registrar vendas**: realizado pelo caixa para contabilizar as compras de fichas.  
- **Emitir fichas com código único**: garante que cada ficha seja única (evitando duplicidade).  
- **Registrar devoluções de fichas**: controla corretamente o saldo de estoque e caixa.  
- **Cadastrar produtos (fardo/unidade)**: permite gerenciar os produtos vendidos.  
- **Definir estoque mínimo**: dispara alertas antes que o produto acabe durante o evento.  
- **Dar baixa automática no estoque**: reduz automaticamente o estoque quando a ficha é validada.  
- **Consultar relatórios de vendas e estoque**: permite visualizar desempenho e perdas por evento.  
- **Comprar fichas**: interação do cliente no caixa para adquirir fichas.  
- **Entregar ficha para receber produto**: interação do cliente no balcão.  
- **Validar fichas**: realizada pelo entregador no momento da entrega.  

---

## 4. Relações entre Atores e Casos de Uso
- O **Administrador** se relaciona com: Definir estoque mínimo, Cadastrar produtos, Consultar relatórios.  
- O **Caixa** se relaciona com: Registrar vendas, Emitir fichas, Registrar devoluções, Validar fichas, Dar baixa no estoque, Consultar relatórios, Cadastrar produtos, Definir estoque mínimo.  
- O **Cliente** se relaciona com: Comprar fichas, Entregar ficha para receber produto.  
- O **Entregador** se relaciona com: Validar fichas, Dar baixa no estoque.  

---

## 5. Observações
- O **Caixa é o principal usuário do sistema** e centraliza a maioria das operações.  
- O **Cliente e o Entregador** interagem mais de forma indireta, mas foram representados no diagrama para dar uma visão completa do fluxo no evento.  
- O **Administrador** pode ser a mesma pessoa que o caixa em eventos menores, mas foi separado para destacar responsabilidades diferentes.  

