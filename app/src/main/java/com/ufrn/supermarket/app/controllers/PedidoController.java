package com.ufrn.supermarket.app.controllers;


import com.ufrn.supermarket.app.dtos.PedidoDTO;
import com.ufrn.supermarket.app.dtos.ProdutoDTO;
import com.ufrn.supermarket.app.entities.PedidoEntity;
import com.ufrn.supermarket.app.services.PedidoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    @Autowired
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<PedidoDTO>> getAll(){
        try{
            List<PedidoDTO> pedidoDTOlist = pedidoService.getAll();
            return ResponseEntity.ok(pedidoDTOlist);
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/list")
    public ResponseEntity<Optional<PedidoEntity>> getById(@PathVariable Long id){
        try {
            Optional<PedidoEntity> pedidoDTO = pedidoService.getById(id);
            return ResponseEntity.ok(pedidoDTO);
        } catch(Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<?> postPedido(@Valid @RequestBody PedidoDTO pedidoDTO){
        try {
            PedidoDTO pedidoCreated = pedidoService.postPedido(pedidoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoCreated);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<PedidoDTO> putPedido(@PathVariable Long id,@Valid @RequestBody PedidoDTO pedidoDTOAtualizado) {
        try {
            PedidoDTO pedidoDTO = pedidoService.putPedido(id, pedidoDTOAtualizado);
            return ResponseEntity.ok(pedidoDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deletePedido(@PathVariable Long id){
        try{
            pedidoService.deletePedido(id);
            return ResponseEntity.ok("Sucesso: Pedido excluído com sucesso!");
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{id}/Softdelete")
    public ResponseEntity<?> deleteLogic(@PathVariable Long id) {
        try {
            pedidoService.deleteLogic(id);
            return ResponseEntity.ok("Sucesso: Pedido desativado com sucesso!");
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Erro: " + ex.getMessage());
        }
    }


    @PutMapping("/{pedidoId}/add")
    public ResponseEntity<?> adicionarProduto(@PathVariable Long pedidoId, @Valid @RequestBody ProdutoDTO produtoDTO) {
        try {
            PedidoDTO pedidoAtualizado = pedidoService.adicionarProduto(pedidoId, produtoDTO);
            return ResponseEntity.ok(pedidoAtualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar produto ao pedido.");
        }
    }


    @DeleteMapping("/{pedidoId}/remove/{produtoId}")
    public ResponseEntity<?> removerProduto(@PathVariable Long pedidoId, @PathVariable Long produtoId) {
        try {
            PedidoDTO pedidoAtualizado = pedidoService.removerProduto(pedidoId, produtoId);
            return ResponseEntity.ok(pedidoAtualizado);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao remover o produto.");
        }
    }



}
