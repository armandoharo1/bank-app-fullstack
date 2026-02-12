import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { CuentasService } from '../../core/services/cuentas.service';
import { MovimientosService } from '../../core/services/movimientos.service';

import { CuentaResponse } from '../../core/models/cuenta.model';
import {
  MovimientoCreateRequest,
  MovimientoResponse,
  MovimientoUpdateRequest,
  TipoMovimiento
} from '../../core/models/movimiento.model';

@Component({
  selector: 'app-movimientos',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './movimientos.component.html',
  styleUrls: ['./movimientos.component.scss'],
})
export class MovimientosComponent {
  cuentas: CuentaResponse[] = [];
  movimientos: MovimientoResponse[] = [];
  filtered: MovimientoResponse[] = [];

  selectedCuenta: string | null = null;

  showForm = false;
  mode: 'create' | 'edit' = 'create';
  editingId: number | null = null;

  readonly tipos: TipoMovimiento[] = ['CREDITO', 'DEBITO'];

  form = this.fb.group({
    //la fecha solo se usa en Edit
    fecha: ['', [Validators.required]],

    numeroCuenta: ['', [Validators.required]],
    tipoMovimiento: ['CREDITO' as TipoMovimiento, [Validators.required]],
    valor: [0, [Validators.required, Validators.min(0.01)]],
  });

  constructor(
    private cuentasApi: CuentasService,
    private movApi: MovimientosService,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.loadCuentas();
  }

  loadCuentas() {
    this.cuentasApi.list().subscribe((res: CuentaResponse[]) => {
      this.cuentas = res;

      if (!this.selectedCuenta && res.length > 0) {
        this.selectedCuenta = res[0].numeroCuenta;
        this.loadMovimientos();
      }
    });
  }

  onSelectCuenta(ev: Event) {
    const v = (ev.target as HTMLSelectElement).value;
    this.selectedCuenta = v || null;
    this.loadMovimientos();
  }

  loadMovimientos() {
    if (!this.selectedCuenta) {
      this.movimientos = [];
      this.filtered = [];
      return;
    }

    this.movApi.listByCuenta(this.selectedCuenta).subscribe((res: MovimientoResponse[]) => {
      const sorted = [...res].sort((a, b) => (b.fecha || '').localeCompare(a.fecha || ''));
      this.movimientos = sorted;
      this.filtered = sorted;
    });
  }

  onSearch(ev: Event) {
    const value = (ev.target as HTMLInputElement).value?.toLowerCase().trim() ?? '';
    if (!value) {
      this.filtered = [...this.movimientos];
      return;
    }

    this.filtered = this.movimientos.filter(m =>
      String(m.movimientoId).includes(value) ||
      (m.tipoMovimiento ?? '').toLowerCase().includes(value) ||
      String(m.valor ?? '').includes(value) ||
      String(m.saldo ?? '').includes(value) ||
      (m.fecha ?? '').toLowerCase().includes(value)
    );
  }

  onNuevo() {
    if (!this.selectedCuenta) {
      alert('Seleccione una cuenta primero.');
      return;
    }

    this.mode = 'create';
    this.editingId = null;

    this.form.enable();

    const today = this.todayAsYYYYMMDD();

    this.form.reset({
      fecha: today,
      numeroCuenta: this.selectedCuenta,
      tipoMovimiento: 'CREDITO',
      valor: 0
    });

    //numeroCuenta se fija por selector, no se edita
    this.form.get('numeroCuenta')?.disable();

    //fecha no es parte del create en backend, disabled
    this.form.get('fecha')?.disable();

    this.showForm = true;
  }

  onEditar(row: MovimientoResponse) {
    this.mode = 'edit';
    this.editingId = Number(row.movimientoId);

    this.form.enable();

    //normalizacion de fecha a YYYY-MM-DD 
    const fecha = this.normalizeToYYYYMMDD(row.fecha);

    this.form.reset({
      fecha,
      numeroCuenta: row.numeroCuenta,
      tipoMovimiento: row.tipoMovimiento as TipoMovimiento,
      valor: Math.abs(Number(row.valor))
    });

    this.form.get('numeroCuenta')?.disable();

    // en edit si se necesita fecha, debe quedar habilitada
    this.form.get('fecha')?.enable();

    this.showForm = true;
  }

  cerrarForm() {
    this.showForm = false;
    this.form.enable();
  }

  guardar() {
    console.log('[MOV] guardar() mode=', this.mode, 'editingId=', this.editingId, 'valid=', this.form.valid);

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      alert('Formulario inválido. Revisa fecha/valor/tipo.');
      return;
    }

    const raw = this.form.getRawValue();
    const tipo = raw.tipoMovimiento as TipoMovimiento;
    const valorPositivo = Math.abs(Number(raw.valor ?? 0));

    if (this.mode === 'create') {
      if (!this.selectedCuenta) return;

      const payload: MovimientoCreateRequest = {
        numeroCuenta: this.selectedCuenta,
        tipoMovimiento: tipo,
        valor: valorPositivo,
      };

      this.movApi.create(payload).subscribe({
        next: () => {
          this.cerrarForm();
          this.loadMovimientos();
        },
        error: (err) => {
          console.error('[MOV] create error', err);
          alert('Error al crear movimiento. Revisa consola/Network.');
        }
      });
      return;
    }

    if (this.editingId == null) {
      alert('No se encontró el ID del movimiento para editar.');
      return;
    }

    //backend exige fecha + tipoMovimiento + valor
    const payload: MovimientoUpdateRequest = {
      fecha: this.normalizeToYYYYMMDD(raw.fecha ?? ''),
      tipoMovimiento: tipo,
      valor: valorPositivo,
    };

    this.movApi.update(this.editingId, payload).subscribe({
      next: () => {
        this.cerrarForm();
        this.loadMovimientos();
      },
      error: (err) => {
        console.error('[MOV] update error', err);
        alert('Error al editar movimiento. Revisa consola/Network.');
      }
    });
  }

  eliminar(row: MovimientoResponse) {
    const ok = confirm(`¿Eliminar movimiento #${row.movimientoId}?`);
    if (!ok) return;

    this.movApi.remove(Number(row.movimientoId)).subscribe(() => {
      this.loadMovimientos();
    });
  }

  fmtFecha(iso: string): string {
    if (!iso) return '';
    return String(iso).replace('T', ' ').slice(0, 19);
  }

  private todayAsYYYYMMDD(): string {
    const d = new Date();
    const yyyy = d.getFullYear();
    const mm = String(d.getMonth() + 1).padStart(2, '0');
    const dd = String(d.getDate()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd}`;
  }

  private normalizeToYYYYMMDD(value: any): string {
    if (!value) return this.todayAsYYYYMMDD();

    const s = String(value);

    if (/^\d{4}-\d{2}-\d{2}$/.test(s)) return s;
    if (s.includes('T')) return s.split('T')[0];

    return s.slice(0, 10);
  }
}
