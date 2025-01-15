package com.ufrn.supermarket.app.dtos;

import java.util.List;

public record PedidoDTO(
                Long id,
                String codigo,
                List<ProdutoDTO> produtos,
                ClienteDTO cliente,
                boolean ativo) {
}
