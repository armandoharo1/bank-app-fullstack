package com.devsu.bank.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cuenta")
public class Cuenta {

    @Id
    @NotBlank
    @Column(name = "numero_cuenta", length = 30, nullable = false)
    private String numeroCuenta;

    @NotBlank
    @Size(max = 30)
    @Column(name = "tipo_cuenta", nullable = false, length = 30)
    private String tipoCuenta;

    @NotNull
    @PositiveOrZero
    @Column(name = "saldo_inicial", nullable = false, precision = 14, scale = 2)
    private BigDecimal saldoInicial;

    @NotNull
    @PositiveOrZero
    @Column(name = "saldo_disponible", nullable = false, precision = 14, scale = 2)
    private BigDecimal saldoDisponible;

    @NotNull
    @Column(nullable = false)
    private Boolean estado;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
}