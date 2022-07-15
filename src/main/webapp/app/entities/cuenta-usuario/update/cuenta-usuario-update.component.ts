import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICuentaUsuario, CuentaUsuario } from '../cuenta-usuario.model';
import { CuentaUsuarioService } from '../service/cuenta-usuario.service';

@Component({
  selector: 'jhi-cuenta-usuario-update',
  templateUrl: './cuenta-usuario-update.component.html',
})
export class CuentaUsuarioUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    balance: [null, [Validators.required]],
    numCanjes: [null, [Validators.required]],
    apuestasTotales: [null, [Validators.required]],
    apuestasGanadas: [null, [Validators.required]],
  });

  constructor(protected cuentaUsuarioService: CuentaUsuarioService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cuentaUsuario }) => {
      this.updateForm(cuentaUsuario);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cuentaUsuario = this.createFromForm();
    if (cuentaUsuario.id !== undefined) {
      this.subscribeToSaveResponse(this.cuentaUsuarioService.update(cuentaUsuario));
    } else {
      this.subscribeToSaveResponse(this.cuentaUsuarioService.create(cuentaUsuario));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICuentaUsuario>>): void {
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

  protected updateForm(cuentaUsuario: ICuentaUsuario): void {
    this.editForm.patchValue({
      id: cuentaUsuario.id,
      balance: cuentaUsuario.balance,
      numCanjes: cuentaUsuario.numCanjes,
      apuestasTotales: cuentaUsuario.apuestasTotales,
      apuestasGanadas: cuentaUsuario.apuestasGanadas,
    });
  }

  protected createFromForm(): ICuentaUsuario {
    return {
      ...new CuentaUsuario(),
      id: this.editForm.get(['id'])!.value,
      balance: this.editForm.get(['balance'])!.value,
      numCanjes: this.editForm.get(['numCanjes'])!.value,
      apuestasTotales: this.editForm.get(['apuestasTotales'])!.value,
      apuestasGanadas: this.editForm.get(['apuestasGanadas'])!.value,
    };
  }
}
