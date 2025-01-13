package com.ufrn.supermarket.app.controllers;

import com.ufrn.supermarket.app.dtos.ClienteDTO;
import com.ufrn.supermarket.app.entities.ClienteEntity;
import com.ufrn.supermarket.app.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public  ClienteController( ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<ClienteDTO>>  findAll() {
        try{
            List<ClienteDTO> clienteDTOList = clienteService.findAll();
            return new ResponseEntity<>(clienteDTOList, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ClienteEntity>> findById(@PathVariable Long id) {

        try{
            Optional<ClienteEntity> clienteDTO = clienteService.findById(id);
            return new ResponseEntity<>(clienteDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createCliente(@RequestBody ClienteDTO clienteDTO) {
        try {
            // Cria o cliente utilizando o serviço
            ClienteDTO createdCliente = clienteService.createCliente(clienteDTO);

            // Retorna o cliente criado com status 201 (CREATED)
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCliente);

        } catch (IllegalArgumentException e) {
            // Retorna erro 400 (BAD REQUEST) para argumentos inválidos
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (DataIntegrityViolationException e) {
            // Trata violação de integridade (ex.: CPF duplicado)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Já existe um cliente com o mesmo CPF.");

        } catch (Exception e) {
            // Erro genérico do servidor
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar cliente.");
        }
    }


    @PutMapping("/edit")
    public ResponseEntity<ClienteDTO> editCliente(@RequestBody ClienteEntity cliente) {
        try {
            ClienteDTO updatedClienteDTO = clienteService.editCliente(cliente);
            if (updatedClienteDTO == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedClienteDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCliente(@PathVariable Long id) {
        try{
            clienteService.deleteCliente(id);
            return ResponseEntity.ok("Sucesso: Cliente excluído com sucesso!");
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
