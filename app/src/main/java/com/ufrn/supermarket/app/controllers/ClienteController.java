package com.ufrn.supermarket.app.controllers;

import com.ufrn.supermarket.app.dtos.ClienteDTO;
import com.ufrn.supermarket.app.entities.ClienteEntity;
import com.ufrn.supermarket.app.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/create")
    public ResponseEntity<ClienteDTO> createCliente(@RequestBody ClienteDTO clienteDTO) {
        try {
            ClienteDTO createdCliente = clienteService.createCliente(clienteDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCliente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
