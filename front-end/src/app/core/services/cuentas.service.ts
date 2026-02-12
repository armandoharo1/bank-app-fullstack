import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CuentaCreateRequest, CuentaResponse, CuentaUpdateRequest } from '../models/cuenta.model';

@Injectable({ providedIn: 'root' })
export class CuentasService {
  private readonly baseUrl = '/api/cuentas';

  constructor(private http: HttpClient) {}

  list(): Observable<CuentaResponse[]> {
    return this.http.get<CuentaResponse[]>(this.baseUrl);
  }

  listByCliente(clienteId: number): Observable<CuentaResponse[]> {
    return this.http.get<CuentaResponse[]>(`${this.baseUrl}/cliente/${clienteId}`);
  }

  getByNumero(numeroCuenta: string): Observable<CuentaResponse> {
    return this.http.get<CuentaResponse>(`${this.baseUrl}/${numeroCuenta}`);
  }

  create(payload: CuentaCreateRequest): Observable<CuentaResponse> {
    return this.http.post<CuentaResponse>(this.baseUrl, payload);
  }

  update(numeroCuenta: string, payload: CuentaUpdateRequest): Observable<CuentaResponse> {
    return this.http.put<CuentaResponse>(`${this.baseUrl}/${numeroCuenta}`, payload);
  }

  remove(numeroCuenta: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${numeroCuenta}`);
  }
}
