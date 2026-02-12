import { FormBuilder } from '@angular/forms';
import { ClientesComponent } from './clientes.component';

describe('ClientesComponent', () => {
  it('onSearch() debe filtrar por nombre', () => {
    const mockApi = { list: jest.fn() } as any;
    const fb = new FormBuilder();

    const comp = new ClientesComponent(mockApi, fb);

    comp.clientes = [
      {
        personaId: 1,
        nombre: 'Armando',
        genero: 'M',
        edad: 30,
        identificacion: 'A',
        direccion: 'X',
        telefono: '1',
        clientKey: 'k1',
        estado: true
      },
      {
        personaId: 2,
        nombre: 'Maria',
        genero: 'F',
        edad: 25,
        identificacion: 'B',
        direccion: 'Y',
        telefono: '2',
        clientKey: 'k2',
        estado: true
      }
    ] as any;

    comp.filtered = [...comp.clientes];

    const ev = { target: { value: 'mari' } } as any;
    comp.onSearch(ev);

    expect(comp.filtered.length).toBe(1);
    expect(comp.filtered[0].nombre).toBe('Maria');
  });
});
