package com.devsu.bank.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ReporteEstadoCuentaResponse(
        Long clienteId,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        BigDecimal totalCreditos,
        BigDecimal totalDebitos,
        List<ReporteMovimientoResponse> movimientos
) {}