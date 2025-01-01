package com.ufrn.supermarket.app.entities;

import com.ufrn.supermarket.app.dtos.ProdutoDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "O código do pedido não pode ser vazio.")
    private String codigo;


    @ManyToMany
    @JoinTable(
            name = "pedido_produto",
            joinColumns = @JoinColumn(name = "pedido_id"),
            inverseJoinColumns = @JoinColumn(name = "produto_id")
    )
    @NotEmpty(message = "O pedido deve ter ao menos um produto.")
    private List<ProdutoEntity> produtos = new ArrayList<ProdutoEntity>();


    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    @NotNull(message = "O cliente não pode ser nulo.")
    private ClienteEntity cliente;

    @Column(nullable = false)
    private boolean ativo = true;


    public void adicionarProduto(ProdutoEntity produto) {
        if (produtos == null) {
            produtos = new ArrayList<ProdutoEntity>();
        }
        produtos.add(produto);
    }

    public void removerProduto(ProdutoEntity produto) {
        if (produtos != null) {
            produtos.remove(produto);
        }
    }
}
