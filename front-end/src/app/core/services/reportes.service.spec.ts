import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { ReportesService } from './reportes.service';

describe('ReportesService', () => {
  let service: ReportesService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ReportesService, provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(ReportesService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('getReporte() debe llamar al endpoint con params fechaInicio/fechaFin', () => {
    service.getReporte(10, '2026-02-01', '2026-02-11').subscribe();

    const req = httpMock.expectOne(r =>
      r.method === 'GET' &&
      r.url === '/api/clientes/10/reportes' &&
      r.params.get('fechaInicio') === '2026-02-01' &&
      r.params.get('fechaFin') === '2026-02-11'
    );

    req.flush({
      clienteId: 10,
      fechaInicio: '2026-02-01',
      fechaFin: '2026-02-11',
      totalCreditos: 0,
      totalDebitos: 0,
      movimientos: [],
    });
  });
});
