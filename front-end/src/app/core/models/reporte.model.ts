export type TipoMovimiento = 'CREDITO' | 'DEBITO';

export interface ReporteMovimientoResponse {
  fecha: string;
  cliente: string;
  numeroCuenta: string;
  tipo: TipoMovimiento;
  saldoInicial: number;
  estado: boolean;
  movimiento: number;
  saldoDisponible: number;
}

export interface ReporteEstadoCuentaResponse {
  clienteId: number;
  fechaInicio: string;
  fechaFin: string;
  totalCreditos: number;
  totalDebitos: number;
  movimientos: ReporteMovimientoResponse[];
}

export interface ReporteEstadoCuentaPdfResponse {
  reporte: ReporteEstadoCuentaResponse;
  pdfBase64: string;
}
