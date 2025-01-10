package com.ufrn.supermarket.app.repositories;

import com.ufrn.supermarket.app.entities.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {
    boolean existsByCpf(String cpf);
}
