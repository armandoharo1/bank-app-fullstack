import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { ClientesService } from '../../core/services/clientes.service';
import { ReportesService } from '../../core/services/reportes.service';

import { ClienteResponse } from '../../core/models/cliente.model';
import { ReporteEstadoCuentaResponse, ReporteEstadoCuentaPdfResponse } from '../../core/models/reporte.model';

@Component({
  selector: 'app-reportes',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './reportes.component.html',
  styleUrls: ['./reportes.component.scss'],
})
export class ReportesComponent {
  clientes: ClienteResponse[] = [];
  loading = false;
  errorMsg: string | null = null;

  reporte: ReporteEstadoCuentaResponse | null = null;

  form = this.fb.group({
    clienteId: [null as number | null, [Validators.required]],
    fechaInicio: ['', [Validators.required]],
    fechaFin: ['', [Validators.required]],
  });

  constructor(
    private fb: FormBuilder,
    private clientesApi: ClientesService,
    private reportesApi: ReportesService
  ) {}

  ngOnInit() {
    this.loadClientes();

    const today = this.todayAsYYYYMMDD();
    const firstDay = this.firstDayOfMonthAsYYYYMMDD();
    this.form.patchValue({ fechaInicio: firstDay, fechaFin: today });
  }

  loadClientes() {
    this.clientesApi.list().subscribe((res: ClienteResponse[]) => {
      this.clientes = res;
      // aqui si hay clientes, selecciona el primero por defecto
      if (res.length > 0 && !this.form.get('clienteId')?.value) {
        this.form.patchValue({ clienteId: res[0].personaId });
      }
    });
  }

  consultar() {
    this.errorMsg = null;
    this.reporte = null;

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const clienteId = Number(this.form.value.clienteId);
    const fechaInicio = String(this.form.value.fechaInicio);
    const fechaFin = String(this.form.value.fechaFin);

    this.loading = true;
    this.reportesApi.getReporte(clienteId, fechaInicio, fechaFin).subscribe({
      next: (rep) => {
        this.reporte = rep;
        this.loading = false;
      },
      error: (err) => {
        console.error('[REP] getReporte error', err);
        this.errorMsg = this.extractError(err) ?? 'Error consultando reporte';
        this.loading = false;
      }
    });
  }

  descargarPdf() {
    this.errorMsg = null;

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const clienteId = Number(this.form.value.clienteId);
    const fechaInicio = String(this.form.value.fechaInicio);
    const fechaFin = String(this.form.value.fechaFin);

    this.loading = true;
    this.reportesApi.getReportePdf(clienteId, fechaInicio, fechaFin).subscribe({
      next: (res: ReporteEstadoCuentaPdfResponse) => {
        //actualiza reporte en pantalla
        this.reporte = res.reporte;

        this.downloadBase64Pdf(
          res.pdfBase64,
          `estado-cuenta-cliente-${clienteId}-${fechaInicio}-a-${fechaFin}.pdf`
        );

        this.loading = false;
      },
      error: (err) => {
        console.error('[REP] getReportePdf error', err);
        this.errorMsg = this.extractError(err) ?? 'Error descargando PDF';
        this.loading = false;
      }
    });
  }

  private downloadBase64Pdf(base64: string, filename: string) {
    //soporta base64 puro o con prefijo data
    const pureBase64 = base64.includes(',') ? base64.split(',')[1] : base64;

    const byteChars = atob(pureBase64);
    const byteNumbers = new Array(byteChars.length);
    for (let i = 0; i < byteChars.length; i++) {
      byteNumbers[i] = byteChars.charCodeAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);
    const blob = new Blob([byteArray], { type: 'application/pdf' });

    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    a.click();
    URL.revokeObjectURL(url);
  }

  fmtMoney(n: any): string {
    const num = Number(n ?? 0);
    return num.toLocaleString('es-PE', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
  }

  private todayAsYYYYMMDD(): string {
    const d = new Date();
    const yyyy = d.getFullYear();
    const mm = String(d.getMonth() + 1).padStart(2, '0');
    const dd = String(d.getDate()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd}`;
  }

  private firstDayOfMonthAsYYYYMMDD(): string {
    const d = new Date();
    const yyyy = d.getFullYear();
    const mm = String(d.getMonth() + 1).padStart(2, '0');
    return `${yyyy}-${mm}-01`;
  }

  private extractError(err: any): string | null {
    if (!err) return null;
    if (typeof err?.error === 'string') return err.error;
    if (err?.error?.message) return err.error.message;
    return null;
  }
}
