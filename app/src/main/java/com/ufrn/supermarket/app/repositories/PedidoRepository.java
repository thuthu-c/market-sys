package com.ufrn.supermarket.app.repositories;

import com.ufrn.supermarket.app.entities.PedidoEntity;
import com.ufrn.supermarket.app.entities.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<PedidoEntity, Long>  {
}
