package com.devsu.bank.controller;

import com.devsu.bank.dto.*;
import com.devsu.bank.service.CuentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CuentaResponse crear(@Valid @RequestBody CuentaCreateRequest req) {
        return cuentaService.crear(req);
    }

    @GetMapping("/{numeroCuenta}")
    public CuentaResponse obtener(@PathVariable String numeroCuenta) {
        return cuentaService.obtener(numeroCuenta);
    }

    @PutMapping("/{numeroCuenta}")
    public CuentaResponse actualizar(@PathVariable String numeroCuenta, @Valid @RequestBody CuentaUpdateRequest req) {
        return cuentaService.actualizar(numeroCuenta, req);
    }

    @DeleteMapping("/{numeroCuenta}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable String numeroCuenta) {
        cuentaService.eliminar(numeroCuenta);
    }

    @GetMapping
    public java.util.List<CuentaResponse> listar() {
        return cuentaService.listar();
    }

    @GetMapping("/cliente/{clienteId}")
    public java.util.List<CuentaResponse> listarPorCliente(@PathVariable Long clienteId) {
        return cuentaService.listarPorCliente(clienteId);
    }
}