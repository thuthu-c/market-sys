package com.ufrn.supermarket.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;  // Importando o JsonIgnore
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "produtos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do produto não pode estar vazio")
    @Column(nullable = false)
    private String nomeProduto;

    @NotBlank(message = "A marca não pode estar vazia")
    @Column(nullable = false)
    private String marca;

    @Column(name = "data_fabricacao", nullable = false)
    private LocalDate dataFabricacao;

    @Column(name = "data_validade", nullable = false)
    private LocalDate dataValidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genero genero;

    @Column(nullable = false)
    private String lote;

//    @ManyToOne(fetch = FetchType.LAZY)  // Relacionamento com ClienteEntity
//    @JoinColumn(name = "cliente_id", nullable = false)  // Definindo a chave estrangeira
//    @JsonIgnore  // Evita o loop de chamada de cliente ao serializar produto
//    private ClienteEntity cliente;  // Referência ao cliente que comprou o produto

    public enum Genero {
        COSMETICO,
        ALIMENTICIO,
        HIGIENE_PESSOAL,
        LIMPEZA,
        OUTRO
    }
}
