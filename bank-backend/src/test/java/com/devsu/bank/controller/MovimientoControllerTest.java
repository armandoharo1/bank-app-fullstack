package com.devsu.bank.controller;

import com.devsu.bank.dto.MovimientoCreateRequest;
import com.devsu.bank.entity.TipoMovimiento;
import com.devsu.bank.exception.BusinessException;
import com.devsu.bank.exception.GlobalExceptionHandler;
import com.devsu.bank.service.MovimientoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MovimientoController.class)
@Import({GlobalExceptionHandler.class, MovimientoControllerTest.MockConfig.class})
class MovimientoControllerTest {

    @org.springframework.beans.factory.annotation.Autowired
    MockMvc mvc;

    @org.springframework.beans.factory.annotation.Autowired
    ObjectMapper objectMapper;

    @org.springframework.beans.factory.annotation.Autowired
    MovimientoService movimientoService; // mock

    @TestConfiguration
    static class MockConfig {
        @Bean
        MovimientoService movimientoService() {
            return Mockito.mock(MovimientoService.class);
        }
    }

    @Test
    void debito_conSaldoInsuficiente_retorna400_yMensaje() throws Exception {
        var req = new MovimientoCreateRequest(
                "225487",
                TipoMovimiento.DEBITO,
                new BigDecimal("999999")
        );

        when(movimientoService.crearMovimiento(Mockito.any(MovimientoCreateRequest.class)))
                .thenThrow(new BusinessException("Saldo no disponible"));

        mvc.perform(post("/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Saldo no disponible"));
    }

    @Test
    void debito_cupoDiarioExcedido_retorna400_yMensaje() throws Exception {
        var req = new MovimientoCreateRequest(
                "225487",
                TipoMovimiento.DEBITO,
                new BigDecimal("1000.01") // cualquier valor que represent exceso
        );

        when(movimientoService.crearMovimiento(Mockito.any(MovimientoCreateRequest.class)))
                .thenThrow(new BusinessException("Cupo diario Excedido"));

        mvc.perform(post("/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Cupo diario Excedido"));
    }
}