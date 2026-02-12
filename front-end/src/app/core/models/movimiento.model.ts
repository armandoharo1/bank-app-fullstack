export type TipoMovimiento = 'CREDITO' | 'DEBITO';

export interface MovimientoResponse {
  movimientoId: number;
  fecha: string;
  tipoMovimiento: TipoMovimiento;
  valor: number;
  saldo: number;
  numeroCuenta: string;
}

export interface MovimientoCreateRequest {
  numeroCuenta: string;
  tipoMovimiento: TipoMovimiento;
  valor: number;
}

export interface MovimientoUpdateRequest {
  fecha: string;
  tipoMovimiento: TipoMovimiento;
  valor: number;
}


