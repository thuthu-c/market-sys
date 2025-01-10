package com.ufrn.supermarket.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;  // Importando o JsonIgnore
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;  // Lista de produtos

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

//    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JsonIgnore  // Evita o loop de chamada de produtos ao serializar cliente
//    private List<ProdutoEntity> produtos;  // Lista de produtos associados ao cliente

    public enum Genero {
        MASCULINO,
        FEMININO,
        OUTRO
    }
}
