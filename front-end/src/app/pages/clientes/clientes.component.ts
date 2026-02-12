import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { ClienteCreateRequest, ClienteResponse, ClienteUpdateRequest } from '../../core/models/cliente.model';
import { ClientesService } from '../../core/services/clientes.service';

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './clientes.component.html',
  styleUrls: ['./clientes.component.scss']
})
export class ClientesComponent {
  clientes: ClienteResponse[] = [];
  filtered: ClienteResponse[] = [];

  showForm = false;
  mode: 'create' | 'edit' = 'create';
  editingId: number | null = null;

  form = this.fb.group({
    nombre: ['', [Validators.required, Validators.maxLength(100)]],
    genero: ['M', [Validators.required]],
    edad: [18, [Validators.required, Validators.min(0), Validators.max(120)]],
    identificacion: ['', [Validators.required, Validators.maxLength(30)]],
    direccion: ['', [Validators.required, Validators.maxLength(200)]],
    telefono: ['', [Validators.required, Validators.maxLength(30)]],
    clientKey: ['', [Validators.required, Validators.maxLength(50)]],
    password: [''],
    estado: [true, [Validators.required]]
  });

  constructor(private api: ClientesService, private fb: FormBuilder) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.api.list().subscribe((res: ClienteResponse[]) => {
      this.clientes = res;
      this.filtered = res;
    });
  }

  onSearch(ev: Event) {
    const value = (ev.target as HTMLInputElement).value?.toLowerCase().trim() ?? '';
    if (!value) {
      this.filtered = [...this.clientes];
      return;
    }

    this.filtered = this.clientes.filter(c =>
      String(c.personaId).includes(value) ||
      (c.nombre ?? '').toLowerCase().includes(value) ||
      (c.identificacion ?? '').toLowerCase().includes(value) ||
      (c.telefono ?? '').toLowerCase().includes(value) ||
      (c.clientKey ?? '').toLowerCase().includes(value)
    );
  }

  onNuevo() {
    this.mode = 'create';
    this.editingId = null;

    this.form.enable();
    this.form.reset({
      nombre: '',
      genero: 'M',
      edad: 18,
      identificacion: '',
      direccion: '',
      telefono: '',
      clientKey: '',
      password: '',
      estado: true
    });

    // en el create deben estar habilitados
    this.form.get('identificacion')?.enable();
    this.form.get('clientKey')?.enable();
    this.form.get('password')?.enable();

    this.showForm = true;
  }

  onEditar(row: ClienteResponse) {
    this.mode = 'edit';
    this.editingId = row.personaId;

    this.form.enable();
    this.form.reset({
      nombre: row.nombre,
      genero: row.genero,
      edad: row.edad,
      identificacion: row.identificacion,//solo mostrar
      direccion: row.direccion,
      telefono: row.telefono,
      clientKey: row.clientKey,//solo mostrar
      password: '',//no editar
      estado: row.estado
    });

    // en edit, no se edita identificacion, clientKey y password
    this.form.get('identificacion')?.disable();
    this.form.get('clientKey')?.disable();
    this.form.get('password')?.disable();

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

    if (this.mode === 'create') {
      const raw = this.form.getRawValue();

      if (!raw.password) {
        this.form.get('password')?.setErrors({ required: true });
        return;
      }

      const payload: ClienteCreateRequest = {
        nombre: raw.nombre ?? '',
        genero: raw.genero ?? 'M',
        edad: Number(raw.edad ?? 0),
        identificacion: raw.identificacion ?? '',
        direccion: raw.direccion ?? '',
        telefono: raw.telefono ?? '',
        clientKey: raw.clientKey ?? '',
        password: raw.password ?? '',
        estado: Boolean(raw.estado)
      };

      this.api.create(payload).subscribe(() => {
        this.cerrarForm();
        this.load();
      });
      return;
    }

    if (this.editingId == null) return;

    const payload: ClienteUpdateRequest = {
      nombre: this.form.get('nombre')?.value ?? '',
      genero: this.form.get('genero')?.value ?? 'M',
      edad: Number(this.form.get('edad')?.value ?? 0),
      direccion: this.form.get('direccion')?.value ?? '',
      telefono: this.form.get('telefono')?.value ?? '',
      estado: Boolean(this.form.get('estado')?.value)
    };

    this.api.update(this.editingId, payload).subscribe(() => {
      this.cerrarForm();
      this.load();
    });
  }

  eliminar(row: ClienteResponse) {
    const ok = confirm(`¿Eliminar cliente "${row.nombre}"?`);
    if (!ok) return;

    this.api.remove(row.personaId).subscribe(() => this.load());
  }
}
