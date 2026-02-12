package com.devsu.bank.service;

import com.devsu.bank.dto.MovimientoCreateRequest;
import com.devsu.bank.dto.MovimientoDetailResponse;
import com.devsu.bank.dto.MovimientoResponse;
import com.devsu.bank.dto.MovimientoUpdateRequest;
import com.devsu.bank.entity.Cuenta;
import com.devsu.bank.entity.Movimiento;
import com.devsu.bank.entity.TipoMovimiento;
import com.devsu.bank.exception.BusinessException;
import com.devsu.bank.repository.CuentaRepository;
import com.devsu.bank.repository.MovimientoRepository;
import com.devsu.bank.service.strategy.CreditoStrategy;
import com.devsu.bank.service.strategy.DebitoStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoService {

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    private final CreditoStrategy creditoStrategy;
    private final DebitoStrategy debitoStrategy;

    @Transactional
    public MovimientoResponse crearMovimiento(MovimientoCreateRequest req) {
        Cuenta cuenta = cuentaRepository.findById(req.numeroCuenta())
                .orElseThrow(() -> new BusinessException("Cuenta no encontrada"));

        LocalDate hoy = LocalDate.now();

        Movimiento movimiento = switch (req.tipoMovimiento()) {
            case CREDITO -> creditoStrategy.apply(cuenta, req.valor(), hoy);
            case DEBITO -> debitoStrategy.apply(cuenta, req.valor(), hoy);
        };

        // se guard el movimiento
        Movimiento saved = movimientoRepository.save(movimiento);

        cuenta.setSaldoDisponible(saved.getSaldo());
        cuentaRepository.save(cuenta);

        // se convirte  Entity a DTO
        return new MovimientoResponse(
                getMovimientoId(saved),
                saved.getFecha(),
                saved.getTipoMovimiento().name(),
                saved.getValor(),
                saved.getSaldo(),
                saved.getCuenta().getNumeroCuenta()
        );
    }

    private Long getMovimientoId(Movimiento m) {
        return m.getId();
    }

    private void recalcularSaldos(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new BusinessException("Cuenta no encontrada"));

        BigDecimal saldo = cuenta.getSaldoInicial();

        List<Movimiento> movimientos = movimientoRepository
                .findByCuenta_NumeroCuentaOrderByFechaAscIdAsc(numeroCuenta);

        for (Movimiento m : movimientos) {
            BigDecimal valor = m.getValor();
            saldo = saldo.add(valor);
            m.setSaldo(saldo);
        }

        movimientoRepository.saveAll(movimientos);

        cuenta.setSaldoDisponible(saldo);
        cuentaRepository.save(cuenta);
    }


    public MovimientoDetailResponse getById(Long id) {
        Movimiento mov = movimientoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Movimiento no encontrado"));

        return new MovimientoDetailResponse(
                mov.getId(),
                mov.getFecha(),
                mov.getTipoMovimiento().name(),
                mov.getValor(),
                mov.getSaldo(),
                mov.getCuenta().getNumeroCuenta()
        );
    }

    @Transactional
    public MovimientoDetailResponse update(Long id, MovimientoUpdateRequest req) {
        Movimiento mov = movimientoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Movimiento no encontrado"));

        String numeroCuenta = mov.getCuenta().getNumeroCuenta();

        mov.setFecha(req.fecha());
        mov.setTipoMovimiento(req.tipoMovimiento());

        BigDecimal valor = req.valor();
        if (req.tipoMovimiento() == TipoMovimiento.DEBITO && valor.signum() > 0) valor = valor.negate();
        if (req.tipoMovimiento() == TipoMovimiento.CREDITO && valor.signum() < 0) valor = valor.abs();
        mov.setValor(valor);

        movimientoRepository.save(mov);

        recalcularSaldos(numeroCuenta);

        // releer para retornar saldo ya recalculado
        Movimiento actualizado = movimientoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Movimiento no encontrado"));

        return new MovimientoDetailResponse(
                actualizado.getId(),
                actualizado.getFecha(),
                actualizado.getTipoMovimiento().name(),
                actualizado.getValor(),
                actualizado.getSaldo(),
                actualizado.getCuenta().getNumeroCuenta()
        );
    }

    @Transactional
    public void delete(Long id) {
        Movimiento mov = movimientoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Movimiento no encontrado"));
        String numeroCuenta = mov.getCuenta().getNumeroCuenta();

        movimientoRepository.delete(mov);

        recalcularSaldos(numeroCuenta);
    }


    @Transactional(readOnly = true)
    public List<MovimientoResponse> listar() {
        return movimientoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MovimientoResponse> listarPorCuenta(String numeroCuenta) {
        return movimientoRepository.findByCuenta_NumeroCuenta(numeroCuenta)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private MovimientoResponse toResponse(Movimiento m) {
        return new MovimientoResponse(
                m.getId(),
                m.getFecha(),
                m.getTipoMovimiento().name(),
                m.getValor(),
                m.getSaldo(),
                m.getCuenta().getNumeroCuenta()
        );
    }

}