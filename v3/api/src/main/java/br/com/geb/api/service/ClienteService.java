package br.com.geb.api.service;

import br.com.geb.api.domain.cliente.Cliente;
import br.com.geb.api.exception.ResourceNotFoundException;
import br.com.geb.api.repository.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository){
        this.clienteRepository = clienteRepository;
    }

    public Cliente buscarPorId(Long id){
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: id " + id));
    }

}
