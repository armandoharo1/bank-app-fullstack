package com.devsu.bank.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CuentaCreateRequest(
        @NotBlank @Size(max = 30) String numeroCuenta,
        @NotBlank @Size(max = 30) String tipoCuenta,
        @NotNull @PositiveOrZero BigDecimal saldoInicial,
        @NotNull Boolean estado,
        @NotNull Long clienteId
) {}