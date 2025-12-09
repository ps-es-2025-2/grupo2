package br.com.geb.api.dto;

import lombok.Data;

@Data
public class ProdutoRequest {
    private String nome;
    private Double preco;
    private String categoria;
    private Integer quantidade;

}