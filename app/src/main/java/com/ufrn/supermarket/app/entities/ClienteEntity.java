package com.ufrn.supermarket.app.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome não pode estar vazio")
    private String nome;

    @NotBlank(message = "O CPF não pode estar vazio")
    @Column(unique = true)
    private String cpf;

    @Enumerated(EnumType.STRING)
    private Genero genero;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;


    @NotNull(message = "O campo ativo não pode ser nulo")
    @Column(nullable = false)
    private Boolean ativo;

    public enum Genero {
        MASCULINO,
        FEMININO,
        OUTRO
    }
}
