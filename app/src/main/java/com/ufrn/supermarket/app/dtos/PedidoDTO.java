package com.ufrn.supermarket.app.dtos;

import com.ufrn.supermarket.app.entities.ClienteEntity;

import java.util.List;

public record PedidoDTO(
        Long id,
        String codigo,
        List<ProdutoDTO> produtos,
        ClienteDTO cliente,
        boolean ativo
) { }
