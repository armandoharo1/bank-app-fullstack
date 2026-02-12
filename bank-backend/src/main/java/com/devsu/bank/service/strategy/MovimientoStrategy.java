package com.devsu.bank.service.strategy;

import com.devsu.bank.entity.Cuenta;
import com.devsu.bank.entity.Movimiento;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface MovimientoStrategy {
    Movimiento apply(Cuenta cuenta, BigDecimal valor, LocalDate fecha);
}