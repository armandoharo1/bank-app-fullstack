package com.devsu.bank.service.strategy;

import com.devsu.bank.entity.Cuenta;
import com.devsu.bank.entity.Movimiento;
import com.devsu.bank.entity.TipoMovimiento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CreditoStrategy implements MovimientoStrategy {

    @Override
    public Movimiento apply(Cuenta cuenta, BigDecimal valor, LocalDate fecha) {
        BigDecimal saldoActual = cuenta.getSaldoDisponible();
        BigDecimal nuevoSaldo = saldoActual.add(valor.abs());

        return Movimiento.builder()
                .fecha(fecha)
                .tipoMovimiento(TipoMovimiento.CREDITO)
                .valor(valor.abs()) // positivo
                .saldo(nuevoSaldo)
                .cuenta(cuenta)
                .build();
    }
}