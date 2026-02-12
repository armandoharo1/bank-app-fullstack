package com.devsu.bank.dto;

import java.math.BigDecimal;

public record CuentaResponse(
        String numeroCuenta,
        String tipoCuenta,
        BigDecimal saldoInicial,
        Boolean estado,
        Long clienteId
) {}