package br.com.geb.api.service;

import br.com.geb.api.domain.caixa.Sangria;
import br.com.geb.api.exception.ResourceNotFoundException;
import br.com.geb.api.repository.SangriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SangriaService {

    private final SangriaRepository sangriaRepository;
    private final CaixaService caixaService;

    public SangriaService(SangriaRepository sangriaRepository, CaixaService caixaService) {
        this.sangriaRepository = sangriaRepository;
        this.caixaService = caixaService;
    }

    @Transactional
    public Sangria criar(Sangria sangria, Long caixaId) {
        // Registra a saída no caixa
        caixaService.registrarSaida(caixaId, sangria.getValor());
        
        return sangriaRepository.save(sangria);
    }
    
    public Sangria criar(Sangria sangria) {
        return sangriaRepository.save(sangria);
    }

    public Sangria buscarPorId(Long id) {
        return sangriaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sangria não encontrada: id " + id));
    }

    public List<Sangria> listarTodos() {
        return sangriaRepository.findAll();
    }

    public void deletar(Long id) {
        if (!sangriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sangria não encontrada: id " + id);
        }
        sangriaRepository.deleteById(id);
    }

    public Sangria atualizar(Long id, Sangria novaSangria) {
        Sangria existente = sangriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Sangria não encontrada: id " + id));

        existente.setValor(novaSangria.getValor());
        existente.setJustificativa(novaSangria.getJustificativa());
        existente.setOperadorUsername(novaSangria.getOperadorUsername());

        return sangriaRepository.save(existente);
    }
}

