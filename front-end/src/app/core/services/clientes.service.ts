import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {
  ClienteCreateRequest,
  ClienteResponse,
  ClienteUpdateRequest
} from '../models/cliente.model';

@Injectable({ providedIn: 'root' })
export class ClientesService {
  private readonly baseUrl = '/api/clientes';

  constructor(private http: HttpClient) {}

  list(): Observable<ClienteResponse[]> {
    return this.http.get<ClienteResponse[]>(this.baseUrl);
  }

  getById(id: number): Observable<ClienteResponse> {
    return this.http.get<ClienteResponse>(`${this.baseUrl}/${id}`);
  }

  create(payload: ClienteCreateRequest): Observable<ClienteResponse> {
    return this.http.post<ClienteResponse>(this.baseUrl, payload);
  }

  update(id: number, payload: ClienteUpdateRequest): Observable<ClienteResponse> {
    return this.http.put<ClienteResponse>(`${this.baseUrl}/${id}`, payload);
  }

  remove(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
