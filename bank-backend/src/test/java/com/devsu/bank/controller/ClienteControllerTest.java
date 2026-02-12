package com.devsu.bank.controller;

import com.devsu.bank.dto.ClienteCreateRequest;
import com.devsu.bank.dto.ClienteResponse;
import com.devsu.bank.exception.GlobalExceptionHandler;
import com.devsu.bank.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClienteController.class)
@Import({GlobalExceptionHandler.class, ClienteControllerTest.MockConfig.class})
class ClienteControllerTest {

    @org.springframework.beans.factory.annotation.Autowired
    MockMvc mvc;

    @org.springframework.beans.factory.annotation.Autowired
    ObjectMapper objectMapper;

    @org.springframework.beans.factory.annotation.Autowired
    ClienteService clienteService; // este es el mock

    @TestConfiguration
    static class MockConfig {
        @Bean
        ClienteService clienteService() {
            return Mockito.mock(ClienteService.class);
        }
    }

    @Test
    void crearCliente_retorna201_y_camposClave() throws Exception {
        var req = new ClienteCreateRequest(
                "Juan Osorio",
                "M",
                30,
                "2222222222",
                "13 junio",
                "0988874587",
                "juan.osorio",
                "1245",
                true
        );

        var resp = new ClienteResponse(
                1L,
                req.nombre(),
                req.genero(),
                req.edad(),
                req.identificacion(),
                req.direccion(),
                req.telefono(),
                req.clientKey(),
                req.estado()
        );

        when(clienteService.crear(Mockito.any(ClienteCreateRequest.class))).thenReturn(resp);

        mvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.personaId").value(1))
                .andExpect(jsonPath("$.clientKey").value("juan.osorio"));
    }
}