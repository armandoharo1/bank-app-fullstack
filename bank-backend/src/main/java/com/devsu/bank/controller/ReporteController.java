package com.devsu.bank.controller;

import com.devsu.bank.dto.ReporteEstadoCuentaPdfResponse;
import com.devsu.bank.dto.ReporteEstadoCuentaResponse;
import com.devsu.bank.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    // Devuelve el reporte en JSON + PDF base64 en una sola respuesta
    @GetMapping("/reportes")
    public ReporteEstadoCuentaPdfResponse reporte(
            @RequestParam Long clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        return reporteService.reportePdf(clienteId, fechaInicio, fechaFin);
    }

     // Devuelve solo JSON (sin PDF)
    @GetMapping("/clientes/{clienteId}/reportes")
    public ReporteEstadoCuentaResponse reportePorCliente(
            @PathVariable Long clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        return reporteService.reporte(clienteId, fechaInicio, fechaFin);
    }

     // Devuelve JSON + PDF base64
    @GetMapping("/clientes/{clienteId}/reportes/pdf")
    public ReporteEstadoCuentaPdfResponse reportePdfPorCliente(
            @PathVariable Long clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        return reporteService.reportePdf(clienteId, fechaInicio, fechaFin);
    }
}