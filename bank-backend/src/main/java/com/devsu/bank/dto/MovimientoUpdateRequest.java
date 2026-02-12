package com.devsu.bank.dto;
import com.devsu.bank.entity.TipoMovimiento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record MovimientoUpdateRequest(
        @NotNull LocalDate fecha,
        @NotNull TipoMovimiento tipoMovimiento,
        @NotNull @DecimalMin(value = "0.01") BigDecimal valor
) {}