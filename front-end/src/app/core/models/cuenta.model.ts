export type TipoCuenta = 'AHORRO' | 'CORRIENTE';

export interface CuentaResponse {
  numeroCuenta: string;
  tipoCuenta: TipoCuenta;
  saldoInicial: number;
  estado: boolean;
  clienteId: number;
}

export interface CuentaCreateRequest {
  numeroCuenta: string;
  tipoCuenta: TipoCuenta;
  saldoInicial: number;
  estado: boolean;
  clienteId: number;
}

export interface CuentaUpdateRequest {
  tipoCuenta: TipoCuenta;
  saldoInicial: number;
  estado: boolean;
  clienteId: number;
}
