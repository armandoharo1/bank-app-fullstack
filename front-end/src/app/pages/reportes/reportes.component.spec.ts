import { TestBed } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { ReportesComponent } from './reportes.component';

describe('ReportesComponent', () => {
  it('consultar() no debe llamar API si form inválido', () => {
    const mockClientes = { list: jest.fn() } as any;
    const mockReportes = { getReporte: jest.fn() } as any;

    const fb = new FormBuilder();

    const comp = new ReportesComponent(fb, mockClientes, mockReportes);

    comp.form.patchValue({ clienteId: null, fechaInicio: '', fechaFin: '' });
    comp.consultar();

    expect(mockReportes.getReporte).not.toHaveBeenCalled();
  });
});
