package br.com.geb.api.controller;

import br.com.geb.api.domain.venda.FichaDigital;
import br.com.geb.api.service.FichaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fichas")
public class FichaController {

    private final FichaService fichaService;

    public FichaController(FichaService fichaService){ this.fichaService = fichaService; }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> buscar(@PathVariable String codigo){
        return fichaService.findByCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/validar/{codigo}")
    public ResponseEntity<?> validar(@PathVariable String codigo){
        var op = fichaService.findByCodigo(codigo);
        if(op.isEmpty()) return ResponseEntity.notFound().build();
        FichaDigital f = op.get();
        if(f.getStatus() == br.com.geb.api.enums.StatusFicha.UTILIZADA) return ResponseEntity.status(409).body("Ficha já usada");
        f.setStatus(br.com.geb.api.enums.StatusFicha.UTILIZADA);
        fichaService.salvar(f);
        return ResponseEntity.ok(f);
    }
}
