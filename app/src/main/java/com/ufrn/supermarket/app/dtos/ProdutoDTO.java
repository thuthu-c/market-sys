package com.ufrn.supermarket.app.dtos;

import java.time.LocalDate;

public record ProdutoDTO(
                Long id,
                String nomeProduto,
                String marca,
                LocalDate dataFabricacao,
                LocalDate dataValidade,
                String genero,
                String lote) {
}
