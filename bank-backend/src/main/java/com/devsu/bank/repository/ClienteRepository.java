package com.devsu.bank.repository;

import com.devsu.bank.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByIdentificacion(String identificacion);

    boolean existsByClientId(String clientId);

}