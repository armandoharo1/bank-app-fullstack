package com.devsu.bank.dto;

public record ClienteResponse(
        Long personaId,
        String nombre,
        String genero,
        Integer edad,
        String identificacion,
        String direccion,
        String telefono,
        String clientKey,
        Boolean estado
) {}