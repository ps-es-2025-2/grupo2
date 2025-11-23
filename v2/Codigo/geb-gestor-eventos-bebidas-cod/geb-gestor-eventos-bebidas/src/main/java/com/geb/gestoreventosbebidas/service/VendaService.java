package com.geb.gestoreventosbebidas.service;

import com.geb.gestoreventosbebidas.model.*;
import com.geb.gestoreventosbebidas.repository.*;
import com.geb.gestoreventosbebidas.enums.TipoMovimentacaoCaixa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.UUID; // Para gerar código único

@Service
public class VendaService {

    @Autowired private VendaRepository vendaRepository;
    @Autowired private ItemVendaRepository itemVendaRepository;
    @Autowired private FichaQRCodeRepository fichaQrCodeRepository;
    @Autowired private EstoqueRepository estoqueRepository;
    @Autowired private CaixaService caixaService; // Usaremos o serviço de caixa

    // CU: Registrar Venda
    @Transactional // Garante que a operação seja atômica (tudo ou nada)
    public Venda registrarVenda(Venda venda, Usuario usuario) {
        // 1. Calcular o valor total e dar baixa no estoque
        double valorTotal = 0.0;

        for (ItemVenda item : venda.getItens()) {
            Estoque estoque = estoqueRepository.findById(item.getBebida().getId()) // Assumindo que o ID da Bebida no item é na verdade o ID do Estoque
                    .orElseThrow(() -> new IllegalArgumentException("Estoque do item não encontrado."));

            if (estoque.getQuantidadeAtual() < item.getQuantidade()) {
                throw new IllegalStateException("Estoque insuficiente para a venda: " + estoque.getBebida().getNome());
            }

            // Atualiza Estoque
            estoque.setQuantidadeAtual(estoque.getQuantidadeAtual() - item.getQuantidade());
            estoqueRepository.save(estoque);

            // Calcula o valor
            item.setPrecoUnitario(estoque.getBebida().getPrecoUnitario()); // Garante o preço unitário correto
            valorTotal += item.getSubtotal();
        }

        // 2. Registrar Venda
        venda.setDataHora(new Date());
        venda.setValorTotal(valorTotal);
        Venda vendaSalva = vendaRepository.save(venda);

        // 3. Registrar a Movimentação de Caixa
        caixaService.realizarOperacao(TipoMovimentacaoCaixa.VENDA, valorTotal, usuario);

        // 4. Gerar e anexar a Ficha QR Code (CU: Emitir Ficha/QR Code)
        FichaQRCode ficha = gerarFichaQRCode(vendaSalva);
        vendaSalva.setFichaQrCode(ficha);

        // O Spring Data JPA salva os itens automaticamente se o 'CascadeType.ALL' foi configurado
        return vendaRepository.save(vendaSalva);
    }

    // Lógica para gerar a ficha (Simulação)
    private FichaQRCode gerarFichaQRCode(Venda venda) {
        FichaQRCode ficha = new FichaQRCode();
        ficha.setVenda(venda);
        ficha.setCodigoUnico(UUID.randomUUID().toString()); // Código único
        ficha.setDataGeracao(new Date());

        // Aqui, um serviço de QR Code real geraria a imagem e converteria para Base64
        ficha.setQrCodeBase64("BASE64_DO_QRCODE_" + ficha.getCodigoUnico());

        return fichaQrCodeRepository.save(ficha);
    }
}