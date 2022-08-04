import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMision, Mision } from '../mision.model';
import { MisionService } from '../service/mision.service';

@Component({
  selector: 'jhi-mision-update',
  templateUrl: './mision-update.component.html',
})
export class MisionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.maxLength(100)]],
    descripcion: [null, [Validators.required, Validators.maxLength(200)]],
    bonoCreditos: [null, [Validators.required]],
    dia: [null, [Validators.required, Validators.maxLength(15)]],
    tipo: [null, [Validators.required, Validators.maxLength(15)]],
    opcion1: [null, [Validators.maxLength(50)]],
    opcion2: [null, [Validators.maxLength(50)]],
    opcion3: [null, [Validators.maxLength(50)]],
    opcion4: [null, [Validators.maxLength(50)]],
    enlace: [null, [Validators.maxLength(125)]],
    opcionCorrecta: [],
  });

  constructor(protected misionService: MisionService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mision }) => {
      this.updateForm(mision);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mision = this.createFromForm();
    if (mision.id !== undefined) {
      this.subscribeToSaveResponse(this.misionService.update(mision));
    } else {
      this.subscribeToSaveResponse(this.misionService.create(mision));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMision>>): void {
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

  protected updateForm(mision: IMision): void {
    this.editForm.patchValue({
      id: mision.id,
      nombre: mision.nombre,
      descripcion: mision.descripcion,
      bonoCreditos: mision.bonoCreditos,
      dia: mision.dia,
      tipo: mision.tipo,
      opcion1: mision.opcion1,
      opcion2: mision.opcion2,
      opcion3: mision.opcion3,
      opcion4: mision.opcion4,
      enlace: mision.enlace,
      opcionCorrecta: mision.opcionCorrecta,
    });
  }

  protected createFromForm(): IMision {
    return {
      ...new Mision(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      bonoCreditos: this.editForm.get(['bonoCreditos'])!.value,
      dia: this.editForm.get(['dia'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
      opcion1: this.editForm.get(['opcion1'])!.value,
      opcion2: this.editForm.get(['opcion2'])!.value,
      opcion3: this.editForm.get(['opcion3'])!.value,
      opcion4: this.editForm.get(['opcion4'])!.value,
      enlace: this.editForm.get(['enlace'])!.value,
      opcionCorrecta: this.editForm.get(['opcionCorrecta'])!.value,
    };
  }
}
