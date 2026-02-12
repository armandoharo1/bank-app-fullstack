package com.devsu.bank.dto;

import jakarta.validation.constraints.*;

public record ClienteUpdateRequest(
        @NotBlank @Size(max = 120) String nombre,
        @NotBlank @Size(max = 20) String genero,
        @NotNull @Min(0) Integer edad,
        @NotBlank @Size(max = 200) String direccion,
        @NotBlank @Size(max = 30) String telefono,
        @NotNull Boolean estado
) {}