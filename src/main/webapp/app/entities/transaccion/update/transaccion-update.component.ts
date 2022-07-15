import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITransaccion, Transaccion } from '../transaccion.model';
import { TransaccionService } from '../service/transaccion.service';
import { ICuentaUsuario } from 'app/entities/cuenta-usuario/cuenta-usuario.model';
import { CuentaUsuarioService } from 'app/entities/cuenta-usuario/service/cuenta-usuario.service';

@Component({
  selector: 'jhi-transaccion-update',
  templateUrl: './transaccion-update.component.html',
})
export class TransaccionUpdateComponent implements OnInit {
  isSaving = false;

  cuentaUsuariosSharedCollection: ICuentaUsuario[] = [];

  editForm = this.fb.group({
    id: [],
    idCuenta: [null, [Validators.required]],
    fecha: [null, [Validators.required]],
    tipo: [null, [Validators.required, Validators.maxLength(20)]],
    descripcion: [null, [Validators.required, Validators.maxLength(200)]],
    monto: [null, [Validators.required]],
    cuenta: [],
  });

  constructor(
    protected transaccionService: TransaccionService,
    protected cuentaUsuarioService: CuentaUsuarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transaccion }) => {
      this.updateForm(transaccion);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transaccion = this.createFromForm();
    if (transaccion.id !== undefined) {
      this.subscribeToSaveResponse(this.transaccionService.update(transaccion));
    } else {
      this.subscribeToSaveResponse(this.transaccionService.create(transaccion));
    }
  }

  trackCuentaUsuarioById(_index: number, item: ICuentaUsuario): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransaccion>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(transaccion: ITransaccion): void {
    this.editForm.patchValue({
      id: transaccion.id,
      idCuenta: transaccion.idCuenta,
      fecha: transaccion.fecha,
      tipo: transaccion.tipo,
      descripcion: transaccion.descripcion,
      monto: transaccion.monto,
      cuenta: transaccion.cuenta,
    });

    this.cuentaUsuariosSharedCollection = this.cuentaUsuarioService.addCuentaUsuarioToCollectionIfMissing(
      this.cuentaUsuariosSharedCollection,
      transaccion.cuenta
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cuentaUsuarioService
      .query()
      .pipe(map((res: HttpResponse<ICuentaUsuario[]>) => res.body ?? []))
      .pipe(
        map((cuentaUsuarios: ICuentaUsuario[]) =>
          this.cuentaUsuarioService.addCuentaUsuarioToCollectionIfMissing(cuentaUsuarios, this.editForm.get('cuenta')!.value)
        )
      )
      .subscribe((cuentaUsuarios: ICuentaUsuario[]) => (this.cuentaUsuariosSharedCollection = cuentaUsuarios));
  }

  protected createFromForm(): ITransaccion {
    return {
      ...new Transaccion(),
      id: this.editForm.get(['id'])!.value,
      idCuenta: this.editForm.get(['idCuenta'])!.value,
      fecha: this.editForm.get(['fecha'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      monto: this.editForm.get(['monto'])!.value,
      cuenta: this.editForm.get(['cuenta'])!.value,
    };
  }
}
