package com.ufrn.supermarket.app.dtos;

import java.time.LocalDate;
import com.ufrn.supermarket.app.entities.ClienteEntity.Genero;

public record ClienteDTO(
                Long id,
                String nome,
                String cpf,
                Genero genero,
                LocalDate dataNascimento,
                Boolean ativo) {
}
