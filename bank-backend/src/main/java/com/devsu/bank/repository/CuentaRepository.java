package com.devsu.bank.repository;

import com.devsu.bank.entity.Cuenta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuentaRepository extends JpaRepository<Cuenta, String> {
    boolean existsByNumeroCuenta(String numeroCuenta);

    List<Cuenta> findByCliente_Id(Long clienteId);
}

