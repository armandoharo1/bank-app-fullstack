package com.devsu.bank.util;

import com.devsu.bank.dto.ReporteEstadoCuentaResponse;
import com.devsu.bank.dto.ReporteMovimientoResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

public class ReportePdfGenerator {

    private static final DateTimeFormatter DF = DateTimeFormatter.ISO_DATE;

    private ReportePdfGenerator() {}

    public static byte[] generar(ReporteEstadoCuentaResponse r) {
        try (PDDocument doc = new PDDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {

                float margin = 40;
                float y = page.getMediaBox().getHeight() - margin;
                float leading = 14;

                // Título de documento de salida
                y = writeLine(cs, margin, y, PDType1Font.HELVETICA_BOLD, 14,
                        "Estado de Cuenta");
                y -= 6;

                // Cabecera
                y = writeLine(cs, margin, y, PDType1Font.HELVETICA, 11,
                        "ClienteId: " + r.clienteId());
                y = writeLine(cs, margin, y, PDType1Font.HELVETICA, 11,
                        "Rango: " + DF.format(r.fechaInicio()) + " a " + DF.format(r.fechaFin()));
                y = writeLine(cs, margin, y, PDType1Font.HELVETICA, 11,
                        "Total Creditos: " + safe(r.totalCreditos()) + " | Total Debitos: " + safe(r.totalDebitos()));
                y -= 10;

                // Encabezado d tabla
                y = writeLine(cs, margin, y, PDType1Font.HELVETICA_BOLD, 10,
                        "Fecha | Cuenta | Tipo | Mov. | SaldoDisp.");
                y = writeLine(cs, margin, y, PDType1Font.HELVETICA, 10,
                        "---------------------------------------------------------------");

                // Filas
                for (ReporteMovimientoResponse m : r.movimientos()) {
                    String row = DF.format(m.fecha())
                            + " | " + m.numeroCuenta()
                            + " | " + (m.tipo() != null ? m.tipo().name() : "")
                            + " | " + safe(m.movimiento())
                            + " | " + safe(m.saldoDisponible());

                    // Salto de página
                    if (y < margin + 60) {
                        cs.close();

                        PDPage newPage = new PDPage(PDRectangle.A4);
                        doc.addPage(newPage);

                        try (PDPageContentStream cs2 = new PDPageContentStream(doc, newPage)) {
                            y = newPage.getMediaBox().getHeight() - margin;

                            y = writeLine(cs2, margin, y, PDType1Font.HELVETICA_BOLD, 10,
                                    "Fecha | Cuenta | Tipo | Mov. | SaldoDisp.");
                            y = writeLine(cs2, margin, y, PDType1Font.HELVETICA, 10,
                                    "---------------------------------------------------------------");

                            y = writeLine(cs2, margin, y, PDType1Font.HELVETICA, 9, row);
                            y -= 2;

                            doc.save(out);
                            return out.toByteArray();
                        }
                    }

                    y = writeLine(cs, margin, y, PDType1Font.HELVETICA, 9, row);
                    y -= 2;
                }
            }

            doc.save(out);
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF", e);
        }
    }

    private static float writeLine(PDPageContentStream cs, float x, float y,
                                   org.apache.pdfbox.pdmodel.font.PDFont font, int size, String text) throws Exception {
        cs.beginText();
        cs.setFont(font, size);
        cs.newLineAtOffset(x, y);
        cs.showText(text);
        cs.endText();
        return y - 14;
    }

    private static String safe(BigDecimal v) {
        return v == null ? "0" : v.toPlainString();
    }
}