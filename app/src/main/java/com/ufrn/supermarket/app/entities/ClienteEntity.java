package com.ufrn.supermarket.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;  // Importando o JsonIgnore
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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
    @Column(nullable = false)
    private Genero genero;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @NotNull(message = "O campo ativo não pode ser nulo")
    @Column(nullable = false)
    private Boolean ativo;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // Evita loop na serialização
    private List<ProdutoEntity> produtos;  // Lista de produtos associados ao cliente

    public enum Genero {
        MASCULINO,
        FEMININO,
        OUTRO
    }


    public ClienteEntity(Long id, String nome, String cpf, Genero genero, LocalDate dataNascimento, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.genero = genero;
        this.dataNascimento = dataNascimento;
        this.ativo = ativo;
        this.produtos = null; // Evita erro caso a lista de produtos seja omitida
    }
}
