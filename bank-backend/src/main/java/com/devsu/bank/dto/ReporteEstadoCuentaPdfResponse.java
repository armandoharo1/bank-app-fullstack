package com.devsu.bank.dto;

public record ReporteEstadoCuentaPdfResponse(
        ReporteEstadoCuentaResponse reporte,
        String pdfBase64
) {}