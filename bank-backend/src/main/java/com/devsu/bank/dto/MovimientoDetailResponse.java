package com.devsu.bank.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MovimientoDetailResponse(
        Long id,
        LocalDate fecha,
        String tipoMovimiento,
        BigDecimal valor,
        BigDecimal saldo,
        String numeroCuenta
) {}