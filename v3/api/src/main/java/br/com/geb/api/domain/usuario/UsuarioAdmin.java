package br.com.geb.api.domain.usuario;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ADMIN")
public class UsuarioAdmin extends Usuario {

    public UsuarioAdmin() {
        super();
    } //opcional

}

