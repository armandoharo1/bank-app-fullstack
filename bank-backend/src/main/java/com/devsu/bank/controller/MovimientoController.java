package com.devsu.bank.controller;

import com.devsu.bank.dto.MovimientoCreateRequest;
import com.devsu.bank.dto.MovimientoDetailResponse;
import com.devsu.bank.dto.MovimientoResponse;
import com.devsu.bank.dto.MovimientoUpdateRequest;
import com.devsu.bank.entity.Movimiento;
import com.devsu.bank.service.MovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovimientoResponse crear(@Valid @RequestBody MovimientoCreateRequest req) {
        return movimientoService.crearMovimiento(req);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDetailResponse> getMovimiento(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimientoDetailResponse> updateMovimiento(
            @PathVariable Long id,
            @Valid @RequestBody MovimientoUpdateRequest req
    ) {
        return ResponseEntity.ok(movimientoService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovimiento(@PathVariable Long id) {
        movimientoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public java.util.List<MovimientoResponse> listar() {
        return movimientoService.listar();
    }

    @GetMapping("/cuenta/{numeroCuenta}")
    public java.util.List<MovimientoResponse> listarPorCuenta(@PathVariable String numeroCuenta) {
        return movimientoService.listarPorCuenta(numeroCuenta);
    }

}