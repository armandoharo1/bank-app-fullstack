package com.devsu.bank.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "cliente")
@PrimaryKeyJoinColumn(name = "cliente_id")
public class Cliente extends Persona {

    @NotBlank
    @Size(max = 40)
    @Column(name = "client_key", nullable = false, unique = true, length = 40)
    private String clientId;

    @NotBlank
    @Size(max = 200)
    @Column(name = "password_hash", nullable = false, length = 200)
    private String contrasena;

    @NotNull
    @Column(nullable = false)
    private Boolean estado;
}