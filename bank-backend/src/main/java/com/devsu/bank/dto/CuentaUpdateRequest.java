package com.devsu.bank.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CuentaUpdateRequest(
        @NotBlank @Size(max = 30) String tipoCuenta,
        @NotNull @PositiveOrZero BigDecimal saldoInicial,
        @NotNull Boolean estado
) {}