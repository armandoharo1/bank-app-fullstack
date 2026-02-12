export interface ClienteResponse {
  personaId: number;
  nombre: string;
  genero: string;
  edad: number;
  identificacion: string;
  direccion: string;
  telefono: string;
  clientKey: string;
  estado: boolean;
}

export interface ClienteCreateRequest {
  nombre: string;
  genero: string;
  edad: number;
  identificacion: string;
  direccion: string;
  telefono: string;
  clientKey: string;
  password: string;
  estado: boolean;
}

export interface ClienteUpdateRequest {
  nombre: string;
  genero: string;
  edad: number;
  direccion: string;
  telefono: string;
  estado: boolean;
}
