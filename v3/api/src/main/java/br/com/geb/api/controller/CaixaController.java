package br.com.geb.api.controller;

import br.com.geb.api.domain.caixa.Caixa;
import br.com.geb.api.dto.AbrirCaixaRequest;
import br.com.geb.api.dto.FecharCaixaRequest;
import br.com.geb.api.service.CaixaService;
import br.com.geb.api.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/caixa")
public class CaixaController {

    /*
        Responsável por gerenciar a operação de caixas em uma loja ou estabelecimento.

        Abrir caixa (/abrir): inicia o caixa do dia com um saldo inicial.
        Fechar caixa (/{id}/fechar): encerra o caixa do dia registrando o saldo final.
        Buscar caixa (/{id}): consulta os detalhes de um caixa específico.
        Listar caixas (/): retorna todos os caixas cadastrados.
        Deletar caixa (/{id}): remove o caixa do sistema (geralmente apenas admin).\

        Lida com o controle de fluxo de caixa, garantindo que cada caixa só possa ser aberto, fechado ou
        consultado conforme regras do sistema.

        Como relaciona ao negocio:

            1. Abrir caixa: O caixa representa o dinheiro inicial disponível para o dia.
            O saldo inicial não pode ser negativo.
            Permite iniciar operações de vendas, saídas (sangria) e entradas (suprimentos).
            2. Fechar caixa: Calcula e registra o saldo final, incluindo todas as movimentações do dia.
            Permite gerar relatórios financeiros ou fechar o dia fiscal.
            3. Buscar / listar: Permite auditar ou consultar históricos de caixa, útil para contabilidade e controle interno.
            4. Deletar: Permite remover registros de caixa antigos ou inválidos, mas deve ser restrito a administradores.
     */

    private final CaixaService service;
    private final UsuarioService usuarioService;

    public CaixaController(CaixaService service, UsuarioService usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/abrir")
    public ResponseEntity<Caixa> abrir(@RequestBody @Valid AbrirCaixaRequest abrirCaixa) {
        var operador = usuarioService.buscarPorEmail(abrirCaixa.getEmailOperador());
        return ResponseEntity.status(201).body(
            service.abrirCaixa(abrirCaixa.getSaldoInicial(), operador)
        );
    }
    /*
        O @Valid e uma anotacao do Spring usada para validar automaticamente objetos de entrada (DTOs) antes que eles cheguem a logica de negocio.
        Assim sera validado se o valor do saldo inicial do caixa e negativo ou positivo.
        Nao faz sentido o caixa de uma loja iniciar negativo.
    */

    @PostMapping("/{id}/fechar")
    public ResponseEntity<Caixa> fechar(@PathVariable Long id, @Valid @RequestBody FecharCaixaRequest req) {
        return ResponseEntity.ok(service.fecharCaixa(id, req.getSaldoFinal()));
    }


    /*
        O metodo GET e usado para buscar informacoes e/ou listar algo.
        NAO tem body. Os dados necessarios vao pela URL.

        Nesse metodo busca pelas informacoes do caixa atraves do ID passado via Path
    */
    @GetMapping("/{id}")
    public ResponseEntity<Caixa> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    //Lista todos os caixas existentes
    @GetMapping
    public ResponseEntity<List<Caixa>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    //Ira deletar o caixa com o ID passado
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}

