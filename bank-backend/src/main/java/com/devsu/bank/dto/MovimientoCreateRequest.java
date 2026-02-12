package com.devsu.bank.dto;

import com.devsu.bank.entity.TipoMovimiento;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MovimientoCreateRequest(
        @NotNull String numeroCuenta,
        @NotNull TipoMovimiento tipoMovimiento,
        @NotNull BigDecimal valor
) {}