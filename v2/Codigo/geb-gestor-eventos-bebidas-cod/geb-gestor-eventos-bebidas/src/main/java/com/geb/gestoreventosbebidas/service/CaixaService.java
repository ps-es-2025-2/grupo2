package com.geb.gestoreventosbebidas.service;

import com.geb.gestoreventosbebidas.model.*;
import com.geb.gestoreventosbebidas.repository.CaixaRepository;
import com.geb.gestoreventosbebidas.repository.MovimentacaoCaixaRepository;
import com.geb.gestoreventosbebidas.enums.TipoMovimentacaoCaixa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service // Indica que esta classe é um componente de serviço do Spring
public class CaixaService {

    @Autowired private CaixaRepository caixaRepository;
    @Autowired private MovimentacaoCaixaRepository movimentacaoRepository;

    // Método de suporte para encontrar o caixa ativo (simples, em um cenário real seria mais complexo)
    private Caixa buscarCaixaAberto() {
        // Em um projeto real, você faria uma consulta como caixaRepository.findByAbertoTrue()
        // Por simplicidade aqui, buscamos o último caixa e verificamos se está aberto.
        List<Caixa> caixas = caixaRepository.findAll();
        if (!caixas.isEmpty()) {
            Caixa ultimoCaixa = caixas.get(caixas.size() - 1);
            if (ultimoCaixa.isAberto()) {
                return ultimoCaixa;
            }
        }
        throw new IllegalStateException("Nenhum caixa está aberto no momento.");
    }

    // CU: Abrir Caixa
    public Caixa abrirCaixa(double saldoInicial, Usuario usuario) {
        if (caixaRepository.findAll().stream().anyMatch(Caixa::isAberto)) {
            throw new IllegalStateException("Já existe um caixa aberto.");
        }

        Caixa novoCaixa = new Caixa(null, new Date(), null, saldoInicial, saldoInicial, true);
        Caixa caixaSalvo = caixaRepository.save(novoCaixa);

        registrarMovimentacao(caixaSalvo, usuario, TipoMovimentacaoCaixa.ABERTURA, saldoInicial);
        return caixaSalvo;
    }

    // CU: Fechar Caixa
    public Caixa fecharCaixa(double saldoFinal, Usuario usuario) {
        Caixa caixa = buscarCaixaAberto();

        caixa.setAberto(false);
        caixa.setDataFim(new Date());
        caixa.setSaldoFinal(saldoFinal);

        Caixa caixaFechado = caixaRepository.save(caixa);

        // A movimentação de fechamento geralmente não tem valor, pois o valor total já está no saldoFinal.
        registrarMovimentacao(caixaFechado, usuario, TipoMovimentacaoCaixa.FECHAMENTO, 0.0);
        return caixaFechado;
    }

    // CU: Realizar Sangria / Realizar Reforço
    public MovimentacaoCaixa realizarOperacao(TipoMovimentacaoCaixa tipo, double valor, Usuario usuario) {
        if (tipo != TipoMovimentacaoCaixa.SANGRIA && tipo != TipoMovimentacaoCaixa.REFORCO) {
            throw new IllegalArgumentException("Tipo de operação inválido para esta função.");
        }

        Caixa caixa = buscarCaixaAberto();

        // Atualiza o saldo (Sangria subtrai, Reforço soma)
        double novoSaldo = caixa.getSaldoFinal();
        if (tipo == TipoMovimentacaoCaixa.SANGRIA) {
            novoSaldo -= valor;
        } else { // REFORCO
            novoSaldo += valor;
        }

        caixa.setSaldoFinal(novoSaldo);
        caixaRepository.save(caixa); // Salva o caixa com o saldo atualizado

        return registrarMovimentacao(caixa, usuario, tipo, valor);
    }

    // Método auxiliar para registrar qualquer movimentação
    private MovimentacaoCaixa registrarMovimentacao(Caixa caixa, Usuario usuario, TipoMovimentacaoCaixa tipo, double valor) {
        MovimentacaoCaixa movimentacao = new MovimentacaoCaixa(
                null,
                new Date(),
                tipo,
                valor,
                usuario,
                caixa
        );
        return movimentacaoRepository.save(movimentacao);
    }
}