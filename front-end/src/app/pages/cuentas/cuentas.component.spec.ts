import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';
import { CuentasComponent } from './cuentas.component';

describe('CuentasComponent', () => {

  it('should create', () => {

    const mockCuentasApi = {
      list: jest.fn().mockReturnValue(of([])),
      create: jest.fn().mockReturnValue(of({})),
      update: jest.fn().mockReturnValue(of({})),
      remove: jest.fn().mockReturnValue(of({}))
    } as any;

    const mockClientesApi = {
      list: jest.fn().mockReturnValue(of([]))
    } as any;

    const fb = new FormBuilder();

    const comp = new CuentasComponent(
      mockCuentasApi,
      mockClientesApi,
      fb
    );

    expect(comp).toBeTruthy();
  });

});
