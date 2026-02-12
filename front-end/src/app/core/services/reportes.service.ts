import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ReporteEstadoCuentaPdfResponse, ReporteEstadoCuentaResponse } from '../models/reporte.model';

@Injectable({ providedIn: 'root' })
export class ReportesService {
  constructor(private http: HttpClient) {}

  //JSON (sin PDF)
  getReporte(clienteId: number, fechaInicio: string, fechaFin: string): Observable<ReporteEstadoCuentaResponse> {
    const params = new HttpParams()
      .set('fechaInicio', fechaInicio)
      .set('fechaFin', fechaFin);

    return this.http.get<ReporteEstadoCuentaResponse>(`/api/clientes/${clienteId}/reportes`, { params });
  }

  //JSON + PDF base64
  getReportePdf(clienteId: number, fechaInicio: string, fechaFin: string): Observable<ReporteEstadoCuentaPdfResponse> {
    const params = new HttpParams()
      .set('fechaInicio', fechaInicio)
      .set('fechaFin', fechaFin);

    return this.http.get<ReporteEstadoCuentaPdfResponse>(`/api/clientes/${clienteId}/reportes/pdf`, { params });
  }
}
