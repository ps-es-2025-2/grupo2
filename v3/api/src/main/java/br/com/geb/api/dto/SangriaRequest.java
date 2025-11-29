package br.com.geb.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SangriaRequest {
    private Double valor;
    private String justificativa;
    private String operadorUsername;
}

