package com.ufrn.supermarket.app.services;

import com.ufrn.supermarket.app.dtos.ProdutoDTO;
import com.ufrn.supermarket.app.entities.ProdutoEntity;
import com.ufrn.supermarket.app.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Autowired
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public ProdutoDTO convertToDto(ProdutoEntity produtoEntity) {
        return new ProdutoDTO(
                produtoEntity.getId(),
                produtoEntity.getNomeProduto(),
                produtoEntity.getMarca(),
                produtoEntity.getDataFabricacao(),
                produtoEntity.getDataValidade(),
                produtoEntity.getGenero().toString(),  // Supondo que Genero seja um Enum
                produtoEntity.getLote()
        );
    }




    ProdutoEntity convertToEntity(ProdutoDTO produtoDTO) {
        ProdutoEntity.Genero genero;
        try {
            genero = ProdutoEntity.Genero.valueOf(produtoDTO.genero().toUpperCase());
        } catch (IllegalArgumentException e) {
            // Trate a exceção, por exemplo, atribuindo um valor padrão ou lançando outra exceção
            genero = ProdutoEntity.Genero.OUTRO;  // Valor padrão ou outro valor de fallback
        }

        return new ProdutoEntity(
                produtoDTO.id(),
                produtoDTO.nomeProduto(),
                produtoDTO.marca(),
                produtoDTO.dataFabricacao(),
                produtoDTO.dataValidade(),
                genero,
                produtoDTO.lote()
        );
    }


    public List<ProdutoEntity> findByIds(List<Long> ids) {
        return produtoRepository.findAllById(ids);
    }


    public List<ProdutoDTO> findAll() {
        return produtoRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()).reversed();
    }


    public Optional<ProdutoDTO> findById(Long id) {
        return produtoRepository.findById(id)
                .map(this::convertToDto);
    }

    public ResponseEntity<ProdutoDTO> createProduto(ProdutoDTO produtoDTO) {
        ProdutoEntity produtoEntity = convertToEntity(produtoDTO);
        ProdutoEntity savedProduto = produtoRepository.save(produtoEntity);
        ProdutoDTO responseDto = convertToDto(savedProduto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);

    }

    public ProdutoDTO editProduto(ProdutoDTO produtoDTO) {
        if (produtoDTO.id() == null) {
            throw new IllegalArgumentException("produto ID não pode ser nulo.");
        }

        return produtoRepository.findById(produtoDTO.id())
                .map(existingproduto -> {
                    existingproduto.setNomeProduto(produtoDTO.nomeProduto());
                    existingproduto.setMarca(produtoDTO.marca());
                    existingproduto.setGenero(ProdutoEntity.Genero.valueOf(produtoDTO.genero().toUpperCase()));
                    existingproduto.setDataFabricacao(produtoDTO.dataFabricacao());
                    existingproduto.setDataValidade(produtoDTO.dataValidade());
                    existingproduto.setLote(produtoDTO.lote());
                    ProdutoEntity updatedproduto = produtoRepository.save(existingproduto);
                    return convertToDto(updatedproduto); // Conversão acontece aqui
                }).orElse(null); // Retorna null se não encontrado
    }

    public void deleteProduto(Long id) {
        if (produtoRepository.existsById(id)) {
            produtoRepository.deleteById(id);
        }
    }
}
