package com.devsu.bank.service;

import com.devsu.bank.dto.CuentaCreateRequest;
import com.devsu.bank.dto.CuentaResponse;
import com.devsu.bank.dto.CuentaUpdateRequest;
import com.devsu.bank.entity.Cliente;
import com.devsu.bank.entity.Cuenta;
import com.devsu.bank.exception.BusinessException;
import com.devsu.bank.repository.ClienteRepository;
import com.devsu.bank.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public CuentaResponse crear(CuentaCreateRequest req) {

        if (cuentaRepository.existsById(req.numeroCuenta())) {
            throw new BusinessException("Número de cuenta ya existe");
        }

        Cliente cliente = clienteRepository.findById(req.clienteId())
                .orElseThrow(() -> new BusinessException("Cliente no encontrado"));

        Cuenta cuenta = Cuenta.builder()
                .numeroCuenta(req.numeroCuenta())
                .tipoCuenta(req.tipoCuenta())
                .saldoInicial(req.saldoInicial())
                .saldoDisponible(req.saldoInicial())
                .estado(req.estado())
                .cliente(cliente)
                .build();

        Cuenta saved = cuentaRepository.save(cuenta);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public CuentaResponse obtener(String numeroCuenta) {
        Cuenta c = cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new BusinessException("Cuenta no encontrada"));
        return toResponse(c);
    }

    @Transactional
    public CuentaResponse actualizar(String numeroCuenta, CuentaUpdateRequest req) {
        Cuenta c = cuentaRepository.findById(numeroCuenta)
                .orElseThrow(() -> new BusinessException("Cuenta no encontrada"));

        c.setTipoCuenta(req.tipoCuenta());
        c.setSaldoInicial(req.saldoInicial());
        c.setEstado(req.estado());

        Cuenta saved = cuentaRepository.save(c);
        return toResponse(saved);
    }

    @Transactional
    public void eliminar(String numeroCuenta) {
        if (!cuentaRepository.existsById(numeroCuenta)) {
            throw new BusinessException("Cuenta no encontrada");
        }
        cuentaRepository.deleteById(numeroCuenta);
    }

    private CuentaResponse toResponse(Cuenta c) {
        return new CuentaResponse(
                c.getNumeroCuenta(),
                c.getTipoCuenta(),
                c.getSaldoInicial(),
                c.getEstado(),
                c.getCliente().getId()
        );
    }


    @Transactional(readOnly = true)
    public List<CuentaResponse> listar() {
        return cuentaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CuentaResponse> listarPorCliente(Long clienteId) {
        return cuentaRepository.findByCliente_Id(clienteId)
                .stream()
                .map(this::toResponse)
                .toList();
    }
}