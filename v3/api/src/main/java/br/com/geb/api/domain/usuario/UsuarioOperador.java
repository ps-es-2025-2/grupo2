package br.com.geb.api.domain.usuario;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("OPERADOR")
public class UsuarioOperador extends Usuario {

}

