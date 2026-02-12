import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { ClientesService } from '../../core/services/clientes.service';
import { CuentasService } from '../../core/services/cuentas.service';

import { ClienteResponse } from '../../core/models/cliente.model';
import { CuentaCreateRequest, CuentaResponse, CuentaUpdateRequest, TipoCuenta } from '../../core/models/cuenta.model';

@Component({
  selector: 'app-cuentas',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './cuentas.component.html',
  styleUrls: ['./cuentas.component.scss']
})
export class CuentasComponent {
  cuentas: CuentaResponse[] = [];
  filtered: CuentaResponse[] = [];

  clientes: ClienteResponse[] = [];

  showForm = false;
  mode: 'create' | 'edit' = 'create';
  editingNumero: string | null = null;

  // filtro por cliente
  selectedClienteId: number | null = null;

  readonly tipos: TipoCuenta[] = ['AHORRO', 'CORRIENTE'];

  form = this.fb.group({
    numeroCuenta: ['', [Validators.required, Validators.maxLength(30)]],
    tipoCuenta: ['AHORRO' as TipoCuenta, [Validators.required]],
    saldoInicial: [0, [Validators.required, Validators.min(0)]],
    estado: [true, [Validators.required]],
    clienteId: [null as number | null, [Validators.required]]
  });

  constructor(
    private cuentasApi: CuentasService,
    private clientesApi: ClientesService,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.loadClientes();
    this.loadCuentas();
  }

  loadClientes() {
    this.clientesApi.list().subscribe((res: ClienteResponse[]) => {
      this.clientes = res;
    });
  }

  loadCuentas() {
    // en caso haya filtro por cliente, trae por endpoint especifico
    if (this.selectedClienteId != null) {
      this.cuentasApi.listByCliente(this.selectedClienteId).subscribe((res: CuentaResponse[]) => {
        this.cuentas = res;
        this.filtered = res;
      });
      return;
    }

    this.cuentasApi.list().subscribe((res: CuentaResponse[]) => {
      this.cuentas = res;
      this.filtered = res;
    });
  }

  onSearch(ev: Event) {
    const value = (ev.target as HTMLInputElement).value?.toLowerCase().trim() ?? '';
    if (!value) {
      this.filtered = [...this.cuentas];
      return;
    }

    this.filtered = this.cuentas.filter(c =>
      (c.numeroCuenta ?? '').toLowerCase().includes(value) ||
      (c.tipoCuenta ?? '').toLowerCase().includes(value) ||
      String(c.clienteId ?? '').includes(value) ||
      String(c.saldoInicial ?? '').includes(value)
    );
  }

  onFilterCliente(ev: Event) {
    const v = (ev.target as HTMLSelectElement).value;
    this.selectedClienteId = v ? Number(v) : null;
    this.loadCuentas();
  }

  onNuevo() {
    this.mode = 'create';
    this.editingNumero = null;

    this.form.enable();
    this.form.reset({
      numeroCuenta: '',
      tipoCuenta: 'AHORRO',
      saldoInicial: 0,
      estado: true,
      clienteId: null
    });

    // en la seccion de create, numeroCuenta debe estar habilitado
    this.form.get('numeroCuenta')?.enable();

    this.showForm = true;
  }

  onEditar(row: CuentaResponse) {
    this.mode = 'edit';
    this.editingNumero = row.numeroCuenta;

    this.form.enable();
    this.form.reset({
      numeroCuenta: row.numeroCuenta,
      tipoCuenta: row.tipoCuenta,
      saldoInicial: row.saldoInicial,
      estado: row.estado,
      clienteId: row.clienteId
    });

    // en la seccion de edit, no permitir cambiar numeroCuenta
    this.form.get('numeroCuenta')?.disable();

    this.showForm = true;
  }

  cerrarForm() {
    this.showForm = false;
    this.form.enable();
  }

  guardar() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const raw = this.form.getRawValue();

    if (this.mode === 'create') {
      const payload: CuentaCreateRequest = {
        numeroCuenta: String(raw.numeroCuenta ?? '').trim(),
        tipoCuenta: raw.tipoCuenta as TipoCuenta,
        saldoInicial: Number(raw.saldoInicial ?? 0),
        estado: Boolean(raw.estado),
        clienteId: Number(raw.clienteId)
      };

      this.cuentasApi.create(payload).subscribe(() => {
        this.cerrarForm();
        this.loadCuentas();
      });
      return;
    }

    if (!this.editingNumero) return;

    const payload: CuentaUpdateRequest = {
      tipoCuenta: raw.tipoCuenta as TipoCuenta,
      saldoInicial: Number(raw.saldoInicial ?? 0),
      estado: Boolean(raw.estado),
      clienteId: Number(raw.clienteId)
    };

    this.cuentasApi.update(this.editingNumero, payload).subscribe(() => {
      this.cerrarForm();
      this.loadCuentas();
    });
  }

  eliminar(row: CuentaResponse) {
    const ok = confirm(`¿Eliminar la cuenta "${row.numeroCuenta}"?`);
    if (!ok) return;

    this.cuentasApi.remove(row.numeroCuenta).subscribe(() => {
      this.loadCuentas();
    });
  }

  clienteNombre(clienteId: number): string {
    const c = this.clientes.find(x => x.personaId === clienteId);
    return c ? c.nombre : String(clienteId);
  }
}
