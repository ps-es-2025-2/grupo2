package br.com.geb.api.service;

import br.com.geb.api.domain.venda.Venda;
import br.com.geb.api.domain.venda.ItemVenda;
import br.com.geb.api.domain.venda.FichaDigital;
import br.com.geb.api.repository.VendaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VendaService {
    private final VendaRepository vendaRepo;
    private final EstoqueService estoqueService;
    private final FichaService fichaService;

    public VendaService(VendaRepository vendaRepo, EstoqueService estoqueService, FichaService fichaService){
        this.vendaRepo = vendaRepo;
        this.estoqueService = estoqueService;
        this.fichaService = fichaService;
    }

    @Transactional
    public Venda registrarVenda(Venda venda){
        double total = venda.getItens().stream()
                .mapToDouble(ItemVenda::getSubtotal)
                .sum();

        venda.setValorTotal(total);

        FichaDigital ficha = fichaService.gerarFicha();
        venda.setFicha(ficha);

        venda.getItens().forEach(i -> i.setVenda(venda));

        Venda vendaSalva = vendaRepo.save(venda);

        venda.getItens().forEach(i ->
                estoqueService.decrementar(i.getProduto().getId(), i.getQuantidade())
        );
        return vendaSalva;
    }
}
