package com.devsu.bank.repository;

import com.devsu.bank.dto.ReporteMovimientoResponse;
import com.devsu.bank.entity.Movimiento;
import com.devsu.bank.entity.TipoMovimiento;
import com.devsu.bank.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuentaAndTipoMovimientoAndFecha(Cuenta cuenta, TipoMovimiento tipoMovimiento, LocalDate fecha);

    @Query("""
  select new com.devsu.bank.dto.ReporteMovimientoResponse(
    m.fecha,
    cl.nombre,
    c.numeroCuenta,
    m.tipoMovimiento,
    c.saldoInicial,
    c.estado,
    m.valor,
    m.saldo
  )
  from Movimiento m
  join m.cuenta c
  join c.cliente cl
  where cl.id = :clienteId
    and m.fecha between :fechaInicio and :fechaFin
  order by m.fecha asc, m.id asc
""")
    List<ReporteMovimientoResponse> reportePorClienteYRango(
            @Param("clienteId") Long clienteId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );

    List<Movimiento> findByCuenta_NumeroCuentaOrderByFechaAscIdAsc(String numeroCuenta);

    List<Movimiento> findByCuenta_NumeroCuenta(String numeroCuenta);
}