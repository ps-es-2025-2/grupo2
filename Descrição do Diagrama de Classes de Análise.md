3.4.1. Descrição do Diagrama de Classes de Análise
1. Introdução
O Diagrama de Classes de Análise é um artefato importante que serve como ponte entre os requisitos funcionais, descritos nos Casos de Uso, e o design detalhado do sistema. O objetivo deste diagrama para o projeto Gestor de Eventos de Bebidas (GEB) é fornecer uma primeira visão estrutural do software, identificando as principais responsabilidades, os blocos de informação e as interações lógicas necessárias para atender às necessidades dos stakeholders.

Este modelo conceitual é organizado segundo a análise orientada a objetos, que classifica as classes em três estereótipos: Entidade (Entity), Fronteira (Boundary) e Controle (Control). A seguir, detalhamos cada um desses componentes no contexto do sistema GEB.

2. Componentes do Diagrama
As classes do sistema foram agrupadas de acordo com suas responsabilidades, conforme descrito abaixo.

2.1. Classes de Entidade (<<Entity>>)
Representam os dados persistentes e fundamentais do sistema. Elas formam o "coração" informacional do GEB e, geralmente, correspondem às tabelas no banco de dados.

Usuario: Armazena as informações dos operadores do sistema (Administrador, Funcionário), incluindo seus dados de acesso e permissões.

Evento: Modela um evento específico, permitindo que vendas e estoques sejam gerenciados de forma isolada.

Produto: Contém os detalhes de cada item vendido (ex: nome, preço, tipo).

Estoque: Gerencia a quantidade de cada Produto em um determinado Evento, controlando o saldo atual e o nível mínimo para alertas.

Venda: Registra cada transação realizada, associando-a a um Usuario e a um Evento.

ItemVenda: Classe associativa que detalha os produtos e quantidades de uma Venda específica, permitindo que múltiplas fichas sejam compradas em uma única transação.

Ficha: Representa a ficha digital com um código único, que é gerada em uma Venda e posteriormente validada para a entrega do produto.

2.2. Classes de Fronteira (<<Boundary>>)
São as classes com as quais os atores interagem diretamente. Elas formam a interface do sistema, seja através de telas ou outros pontos de contato.

TelaLogin: Interface de autenticação para todos os usuários do sistema.

TelaPDV: Ponto de Venda principal, usada pelo Funcionário para registrar vendas de forma ágil.

TelaValidacaoFicha: Interface utilizada pelo Funcionário no balcão de entrega para validar a ficha do cliente (via leitura de código ou inserção manual).

PainelAdministrativo: Tela central para o Administrador, de onde ele acessa as funcionalidades de gestão.

TelaGestaoEstoque: Formulários e painéis para o Administrador cadastrar produtos e definir o estoque inicial e mínimo para um evento.

TelaRelatorios: Interface que exibe os relatórios de vendas e estoque para o Administrador.

2.3. Classes de Controle (<<Control>>)
Orquestram a lógica de negócio e o fluxo dos Casos de Uso. Elas atuam como intermediárias, recebendo requisições das Classes de Fronteira e manipulando as Classes de Entidade.

ControladorDeSessao: Gerencia o processo de login, validação de credenciais e controle de permissões de acesso.

ControladorDeVenda: Coordena toda a lógica para o caso de uso "Registrar Venda". Ele interage com outros controladores para garantir que, ao registrar uma venda, a ficha seja emitida e o estoque seja atualizado.

ControladorDeFicha: Responsável pela lógica de gerar o código único de uma Ficha e por processar sua validação e baixa.

ControladorDeEstoque: Centraliza as regras de negócio relacionadas ao inventário, como dar baixa em um produto e verificar se o nível mínimo foi atingido.

GeradorDeRelatorio: Executa a lógica de coletar, processar e agrupar os dados de Venda e Estoque para apresentar as informações na TelaRelatorios.

3. Fluxo de Interação: Exemplo de um Caso de Uso
Para ilustrar como esses componentes interagem, consideremos o fluxo do caso de uso "Registrar Venda":

O Funcionário interage com a TelaPDV para selecionar os produtos desejados.

A TelaPDV envia os dados da transação para o ControladorDeVenda.

O ControladorDeVenda assume a coordenação:

Ele invoca o ControladorDeEstoque para verificar a disponibilidade e dar baixa nos produtos vendidos.

Em seguida, invoca o ControladorDeFicha para gerar as fichas com códigos únicos.

Por fim, ele cria e salva os registros nas entidades Venda e ItemVenda.

Após a conclusão, uma mensagem de sucesso é retornada à TelaPDV, que informa ao Funcionário que a operação foi finalizada e as fichas podem ser impressas/emitidas.

4. Conclusão
Este Diagrama de Classes de Análise estabelece um modelo robusto e claro da estrutura do sistema GEB. Ele valida o entendimento dos requisitos e serve como um mapa fundamental para a equipe de desenvolvimento na fase de implementação (item 3.5), garantindo que a arquitetura do software esteja alinhada com os objetivos de negócio e as necessidades dos usuários.
