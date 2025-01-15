package com.ufrn.supermarket.app.repositories;

import com.ufrn.supermarket.app.entities.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoEntity, Long>  {
}
