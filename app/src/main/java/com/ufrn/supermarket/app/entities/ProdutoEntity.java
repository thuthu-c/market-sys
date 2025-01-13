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

    @ManyToOne(fetch = FetchType.LAZY)  // Relacionamento com ClienteEntity
    @JoinColumn(name = "cliente_id", nullable = false)  // Chave estrangeira para cliente
    @JsonIgnore  // Evita loop na serialização
    private ClienteEntity cliente;  // Referência ao cliente que comprou o produto

    public enum Genero {
        COSMETICO,
        ALIMENTICIO,
        HIGIENE_PESSOAL,
        LIMPEZA,
        OUTRO
    }

    public ProdutoEntity(Long id, String nomeProduto, String marca, LocalDate dataFabricacao, LocalDate dataValidade, Genero genero, String lote) {
        this.id = id;
        this.nomeProduto = nomeProduto;
        this.marca = marca;
        this.dataFabricacao = dataFabricacao;
        this.dataValidade = dataValidade;
        this.genero = genero;
        this.lote = lote;
        this.cliente = null; // Evita erro caso o cliente seja omitido
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public LocalDate getDataFabricacao() {
        return dataFabricacao;
    }

    public void setDataFabricacao(LocalDate dataFabricacao) {
        this.dataFabricacao = dataFabricacao;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public ClienteEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }
}
