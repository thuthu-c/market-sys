package com.ufrn.supermarket.app.services;

import com.ufrn.supermarket.app.dtos.ClienteDTO;
import com.ufrn.supermarket.app.entities.ClienteEntity;
import com.ufrn.supermarket.app.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    ClienteDTO convertToDto(ClienteEntity clienteEntity) {
        return new ClienteDTO(
                clienteEntity.getId(),
                clienteEntity.getNome(),
                clienteEntity.getCpf(),
                clienteEntity.getGenero(),
                clienteEntity.getDataNascimento(),
                clienteEntity.getAtivo()
        );
    }

    // Método para converter ClienteDTO em ClienteEntity
    private ClienteEntity convertToEntity(ClienteDTO clienteDTO) {

        return new ClienteEntity(
                clienteDTO.id(),
                clienteDTO.nome(),
                clienteDTO.cpf(),
                clienteDTO.genero(),
                clienteDTO.dataNascimento(),
                true
        );
    }

    public List<ClienteDTO> findAll() {
        return clienteRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<ClienteEntity> findById(Long id) {
        return clienteRepository.findById(id);
    }

    public ClienteDTO createCliente(ClienteDTO clienteDTO) {
        if (clienteRepository.existsByCpf(clienteDTO.cpf())) {
            System.out.println("É DENTRO!!");
            throw new IllegalArgumentException("Já existe um cliente com o CPF: " + clienteDTO.cpf());
        }
        ClienteEntity clienteEntity = convertToEntity(clienteDTO);
        ClienteEntity savedCliente = clienteRepository.save(clienteEntity);
        return convertToDto(savedCliente);
    }

    public ClienteDTO editCliente(ClienteEntity cliente) {
        if (cliente.getId() == null) {
            throw new IllegalArgumentException("Cliente ID não pode ser nulo.");
        }

        return clienteRepository.findById(cliente.getId())
                .map(existingCliente -> {
                    existingCliente.setNome(cliente.getNome());
                    existingCliente.setCpf(cliente.getCpf());
                    existingCliente.setGenero(cliente.getGenero());
                    existingCliente.setDataNascimento(cliente.getDataNascimento());
                    ClienteEntity updatedCliente = clienteRepository.save(existingCliente);
                    return convertToDto(updatedCliente);
                }).orElse(null);
    }

    public void deleteCliente(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
        }
    }
}

