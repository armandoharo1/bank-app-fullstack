import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MovimientoCreateRequest, MovimientoResponse, MovimientoUpdateRequest } from '../models/movimiento.model';

@Injectable({ providedIn: 'root' })
export class MovimientosService {
  private readonly baseUrl = '/api/movimientos';

  constructor(private http: HttpClient) {}

  list(): Observable<MovimientoResponse[]> {
    return this.http.get<MovimientoResponse[]>(this.baseUrl);
  }

  listByCuenta(numeroCuenta: string): Observable<MovimientoResponse[]> {
    return this.http.get<MovimientoResponse[]>(`${this.baseUrl}/cuenta/${numeroCuenta}`);
  }

  create(payload: MovimientoCreateRequest): Observable<MovimientoResponse> {
    return this.http.post<MovimientoResponse>(this.baseUrl, payload);
  }

  update(id: number, payload: MovimientoUpdateRequest): Observable<MovimientoResponse> {
    return this.http.put<MovimientoResponse>(`${this.baseUrl}/${id}`, payload);
  }

  remove(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
