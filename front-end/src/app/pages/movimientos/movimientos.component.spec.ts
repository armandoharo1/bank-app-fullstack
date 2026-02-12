import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';
import { MovimientosComponent } from './movimientos.component';

describe('MovimientosComponent', () => {
  it('onNuevo() debe abrir modal si hay cuenta seleccionada', () => {
    const mockCuentasApi = { list: jest.fn().mockReturnValue(of([])) } as any;
    const mockMovApi = {
      listByCuenta: jest.fn().mockReturnValue(of([])),
      create: jest.fn().mockReturnValue(of({})),
      update: jest.fn().mockReturnValue(of({})),
      remove: jest.fn().mockReturnValue(of({}))
    } as any;

    const fb = new FormBuilder();
    const comp = new MovimientosComponent(mockCuentasApi, mockMovApi, fb);

    // precondición, cuenta seleccionada
    comp.selectedCuenta = '123456';

    comp.onNuevo();

    expect(comp.showForm).toBe(true);
    expect(comp.mode).toBe('create');
  });

  it('onSearch() debe filtrar por tipoMovimiento', () => {
    const mockCuentasApi = { list: jest.fn().mockReturnValue(of([])) } as any;
    const mockMovApi = { listByCuenta: jest.fn().mockReturnValue(of([])) } as any;

    const fb = new FormBuilder();
    const comp = new MovimientosComponent(mockCuentasApi, mockMovApi, fb);

    comp.movimientos = [
      { movimientoId: 1, fecha: '2026-02-11', tipoMovimiento: 'CREDITO', valor: 10, saldo: 10, numeroCuenta: '1' },
      { movimientoId: 2, fecha: '2026-02-11', tipoMovimiento: 'DEBITO', valor: -5, saldo: 5, numeroCuenta: '1' }
    ] as any;

    comp.filtered = [...comp.movimientos];

    const ev = { target: { value: 'deb' } } as any;
    comp.onSearch(ev);

    expect(comp.filtered.length).toBe(1);
    expect(comp.filtered[0].tipoMovimiento).toBe('DEBITO');
  });
});
