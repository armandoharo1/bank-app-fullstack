package com.devsu.bank.service.strategy;

import com.devsu.bank.entity.Cuenta;
import com.devsu.bank.entity.Movimiento;
import com.devsu.bank.entity.TipoMovimiento;
import com.devsu.bank.exception.BusinessException;
import com.devsu.bank.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DebitoStrategy implements MovimientoStrategy {

    private final MovimientoRepository movimientoRepository;
    private final BigDecimal limiteDiario;

    public DebitoStrategy(
            MovimientoRepository movimientoRepository,
            @Value("${bank.limite-diario-retiro:1000.00}") BigDecimal limiteDiario
    ) {
        this.movimientoRepository = movimientoRepository;
        this.limiteDiario = limiteDiario;
    }

    @Override
    public Movimiento apply(Cuenta cuenta, BigDecimal valor, LocalDate fecha) {
        BigDecimal retiro = valor.abs();

        BigDecimal consumidoHoy = movimientoRepository
                .findByCuentaAndTipoMovimientoAndFecha(cuenta, TipoMovimiento.DEBITO, fecha)
                .stream()
                .map(m -> m.getValor().abs())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (consumidoHoy.add(retiro).compareTo(limiteDiario) > 0) {
            throw new BusinessException("Cupo diario Excedido");
        }

        BigDecimal saldoActual = cuenta.getSaldoDisponible();

        if (saldoActual.compareTo(BigDecimal.ZERO) == 0 ||
                saldoActual.compareTo(retiro) < 0) {
            throw new BusinessException("Saldo no disponible");
        }

        BigDecimal nuevoSaldo = saldoActual.subtract(retiro);

        return Movimiento.builder()
                .fecha(fecha)
                .tipoMovimiento(TipoMovimiento.DEBITO)
                .valor(retiro.negate())
                .saldo(nuevoSaldo)
                .cuenta(cuenta)
                .build();
    }
}