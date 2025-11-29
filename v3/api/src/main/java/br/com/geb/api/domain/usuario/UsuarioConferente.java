package br.com.geb.api.domain.usuario;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("CONFERENTE")
public class UsuarioConferente extends Usuario {

}

