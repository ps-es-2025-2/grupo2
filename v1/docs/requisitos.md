# Documentação do Sistema de Vendas de Fichas

## 1. Histórias de Usuário

### Caixa
- **Como** caixa  
  **Quero** registrar as vendas rapidamente  
  **Para que** a fila no caixa não fique grande  

- **Como** caixa  
  **Quero** emitir fichas com código único automaticamente  
  **Para que** não haja confusão nem duplicidade  

- **Como** caixa  
  **Quero** registrar devoluções de fichas  
  **Para que** o estoque e o caixa fiquem corretos ao final do evento  

- **Como** caixa  
  **Quero** cadastrar os produtos (em fardos ou unidades)  
  **Para que** o controle de estoque seja fiel  

- **Como** caixa  
  **Quero** definir o estoque mínimo de cada produto  
  **Para que** eu seja avisado antes que acabe durante o evento  

- **Como** caixa  
  **Quero** que o sistema dê baixa automática no estoque quando uma ficha é validada  
  **Para que** eu não precise fazer a contagem manual depois  

- **Como** caixa  
  **Quero** consultar relatórios de vendas e estoque por evento  
  **Para que** eu saiba quanto vendi, quanto sobrou e se houve perdas  

---

## 2. Requisitos Funcionais

1. O sistema deve permitir o registro de vendas de fichas.  
2. O sistema deve gerar fichas com código único (ex.: QR Code).  
3. O sistema deve registrar devoluções de fichas.  
4. O sistema deve permitir o cadastro de produtos em fardos ou unidades.  
5. O sistema deve permitir definir estoque mínimo por produto.  
6. O sistema deve dar baixa automática no estoque quando uma ficha é validada.  
7. O sistema deve gerar relatórios de vendas e estoque por evento.  

---

## 3. Requisitos Não Funcionais

1. O sistema deve ser fácil de usar, com interface simples para o caixa.  
2. O sistema deve responder rapidamente às operações (ex.: registro de vendas em menos de 2 segundos).  
3. O sistema deve evitar duplicidade de fichas, garantindo unicidade dos códigos.  
4. O sistema deve armazenar dados de forma segura e confiável.  
5. O sistema deve ser acessível em um computador com navegador comum.  

---
