package com.devsu.bank.service;

import com.devsu.bank.dto.ReporteEstadoCuentaResponse;
import com.devsu.bank.dto.ReporteMovimientoResponse;
import com.devsu.bank.exception.BusinessException;
import com.devsu.bank.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.devsu.bank.dto.ReporteEstadoCuentaPdfResponse;
import com.devsu.bank.util.ReportePdfGenerator;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final MovimientoRepository movimientoRepository;

    @Transactional(readOnly = true)
    public ReporteEstadoCuentaResponse reporte(
            Long clienteId,
            LocalDate fechaInicio,
            LocalDate fechaFin
    ) {
        if (fechaInicio == null || fechaFin == null) {
            throw new BusinessException("fechaInicio y fechaFin son obligatorias");
        }
        if (fechaInicio.isAfter(fechaFin)) {
            throw new BusinessException("La fecha inicio no puede ser mayor a la fecha fin");
        }

        List<ReporteMovimientoResponse> data =
                movimientoRepository.reportePorClienteYRango(clienteId, fechaInicio, fechaFin);

        BigDecimal totalCreditos = data.stream()
                .map(ReporteMovimientoResponse::movimiento)
                .filter(v -> v != null && v.signum() > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // aqui devolvemos en positivo, basicamnte para que sea más entendible para total débitos
        BigDecimal totalDebitos = data.stream()
                .map(ReporteMovimientoResponse::movimiento)
                .filter(v -> v != null && v.signum() < 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .abs();

        return new ReporteEstadoCuentaResponse(
                clienteId,
                fechaInicio,
                fechaFin,
                totalCreditos,
                totalDebitos,
                data
        );
    }


    @Transactional(readOnly = true)
    public ReporteEstadoCuentaPdfResponse reportePdf(Long clienteId, LocalDate fechaInicio, LocalDate fechaFin) {
        // aqui s reutiliza el reporte JSON ya armado con totales
        var reporte = reporte(clienteId, fechaInicio, fechaFin);

        byte[] pdfBytes = ReportePdfGenerator.generar(reporte);
        String base64 = Base64.getEncoder().encodeToString(pdfBytes);

        return new ReporteEstadoCuentaPdfResponse(reporte, base64);
    }


}