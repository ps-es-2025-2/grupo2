package com.geb.gestoreventosbebidas.service;

import com.geb.gestoreventosbebidas.model.Venda;
import com.geb.gestoreventosbebidas.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    @Autowired private VendaRepository vendaRepository;

    // CU: Consultar Relatórios / Gerar Relatórios Automáticos

    // Método: gerarRelatorioVendas(Date inicio, Date fim)
    public List<Venda> gerarRelatorioVendas(Date inicio, Date fim) {
        // Em um projeto real, você usaria um método customizado no Repository, como:
        // vendaRepository.findByDataHoraBetween(inicio, fim);

        // Por simplicidade, faremos um filtro em memória:
        return vendaRepository.findAll().stream()
                .filter(v -> v.getDataHora().after(inicio) && v.getDataHora().before(fim))
                .collect(Collectors.toList());
    }

    // Método: rankProdutos()
    public Map<String, Long> rankProdutos(Date inicio, Date fim) {
        // Encontra todas as vendas no período
        List<Venda> vendas = gerarRelatorioVendas(inicio, fim);

        // Agrupa os itens de venda por nome da bebida e soma as quantidades
        return vendas.stream()
                .flatMap(venda -> venda.getItens().stream())
                .collect(Collectors.groupingBy(
                        item -> item.getBebida().getNome(),
                        Collectors.summingLong(item -> item.getQuantidade())
                ))
                // Para ordenar, você precisaria de mais manipulação com Map.Entry, mas o agrupamento está correto.
                ;
    }

    // Método: horarioPico()
    public Map<Integer, Long> horarioPico(Date inicio, Date fim) {
        // Encontra todas as vendas no período
        List<Venda> vendas = gerarRelatorioVendas(inicio, fim);

        // Agrupa as vendas pela hora do dia (0 a 23)
        return vendas.stream()
                .collect(Collectors.groupingBy(
                        venda -> venda.getDataHora().getHours(), // Obtém a hora da dataHora
                        Collectors.counting() // Conta o número de vendas por hora
                ));
    }

    // Método: estoqueProjetado()
    // Este é complexo e depende de previsões, mas o esqueleto do método é:
    public String estoqueProjetado(Long eventoId) {
        // Lógica:
        // 1. Calcular a taxa de consumo por hora (do horarioPico e rankProdutos).
        // 2. Projetar quanto tempo o estoque atual (EstoqueRepository) durará.

        return "Implementação complexa. Retorna projeção de tempo para esgotamento do estoque.";
    }
}