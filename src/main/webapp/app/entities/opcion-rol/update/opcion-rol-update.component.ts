import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IOpcionRol, OpcionRol } from '../opcion-rol.model';
import { OpcionRolService } from '../service/opcion-rol.service';

@Component({
  selector: 'jhi-opcion-rol-update',
  templateUrl: './opcion-rol-update.component.html',
})
export class OpcionRolUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    opcion: [null, [Validators.required, Validators.maxLength(20)]],
    path: [null, [Validators.required, Validators.maxLength(50)]],
    icono: [null, [Validators.required, Validators.maxLength(30)]],
    nombre: [null, [Validators.required, Validators.maxLength(13)]],
  });

  constructor(protected opcionRolService: OpcionRolService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ opcionRol }) => {
      this.updateForm(opcionRol);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const opcionRol = this.createFromForm();
    if (opcionRol.id !== undefined) {
      this.subscribeToSaveResponse(this.opcionRolService.update(opcionRol));
    } else {
      this.subscribeToSaveResponse(this.opcionRolService.create(opcionRol));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOpcionRol>>): void {
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

  protected updateForm(opcionRol: IOpcionRol): void {
    this.editForm.patchValue({
      id: opcionRol.id,
      opcion: opcionRol.opcion,
      path: opcionRol.path,
      icono: opcionRol.icono,
      nombre: opcionRol.nombre,
    });
  }

  protected createFromForm(): IOpcionRol {
    return {
      ...new OpcionRol(),
      id: this.editForm.get(['id'])!.value,
      opcion: this.editForm.get(['opcion'])!.value,
      path: this.editForm.get(['path'])!.value,
      icono: this.editForm.get(['icono'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
    };
  }
}
