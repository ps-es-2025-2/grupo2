package br.com.geb.api.service;

import br.com.geb.api.domain.cliente.Cliente;
import br.com.geb.api.dto.ClienteRequest;
import br.com.geb.api.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository){
        this.clienteRepository = clienteRepository;
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
    }

    public Cliente cadastrarCliente(ClienteRequest cliente) {
        return clienteRepository.save(toCliente(cliente));
    }

    private Cliente toCliente(ClienteRequest request) {
        Cliente cliente = new Cliente();
        cliente.setEmail(request.getEmail());
        cliente.setNome(request.getNome());
        cliente.setTelefone(request.getTelefone());

        return cliente;
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Transactional
    public Cliente cadastrar(ClienteRequest request) {
        Cliente cliente = Cliente.builder()
            .nome(request.getNome())
            .username(request.getUsername())
            .email(request.getEmail())
            .telefone(request.getTelefone())
            .build();

        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente atualizar(Long id, ClienteRequest request) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        cliente.setNome(request.getNome());
        cliente.setEmail(request.getEmail());
        cliente.setTelefone(request.getTelefone());
        cliente.setUsername(request.getUsername());

        return clienteRepository.save(cliente);
    }

    @Transactional
    public void deletar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        clienteRepository.delete(cliente);
    }

    @Transactional
    public Cliente criarCliente(ClienteRequest req) {
        return cadastrar(req);
    }

}
