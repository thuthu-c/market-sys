package com.ufrn.supermarket.app.controllers;

import com.ufrn.supermarket.app.dtos.ProdutoDTO;
import com.ufrn.supermarket.app.entities.ProdutoEntity;
import com.ufrn.supermarket.app.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    @Autowired
    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<ProdutoDTO>> getAll() {
        try{
            List<ProdutoDTO> produtoDTOS = produtoService.findAll();;
            return ResponseEntity.ok().body(produtoDTOS);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ProdutoDTO>> findById(@PathVariable Long id) {
        try{
            Optional<ProdutoDTO> produtoDTO = produtoService.findById(id);
            return ResponseEntity.ok().body(produtoDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduto(@RequestBody @Valid ProdutoDTO produtoDTO) {
        try{
            System.out.println(produtoDTO);
            ProdutoDTO produtoDto = produtoService.createProduto(produtoDTO).getBody();
            return ResponseEntity.ok().body(produtoDto);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("CADE????" + e.getMessage());
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<ProdutoDTO> editProduto(@RequestBody @Valid ProdutoDTO produtoDTO) {
        try{
            ProdutoDTO produtoDto = produtoService.editProduto(produtoDTO);
            return ResponseEntity.ok().body(produtoDto);
        }catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteProduto(@PathVariable Long id) {
        try{
            produtoService.deleteProduto(id);
            return ResponseEntity.ok("Sucesso: Produto excluído com sucesso!");
        }catch (Exception ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
