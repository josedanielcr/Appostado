import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDeporte, Deporte } from '../deporte.model';
import { DeporteService } from '../service/deporte.service';

@Component({
  selector: 'jhi-deporte-update',
  templateUrl: './deporte-update.component.html',
})
export class DeporteUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.maxLength(50)]],
  });

  constructor(protected deporteService: DeporteService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deporte }) => {
      this.updateForm(deporte);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deporte = this.createFromForm();
    if (deporte.id !== undefined) {
      this.subscribeToSaveResponse(this.deporteService.update(deporte));
    } else {
      this.subscribeToSaveResponse(this.deporteService.create(deporte));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeporte>>): void {
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

  protected updateForm(deporte: IDeporte): void {
    this.editForm.patchValue({
      id: deporte.id,
      nombre: deporte.nombre,
    });
  }

  protected createFromForm(): IDeporte {
    return {
      ...new Deporte(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
    };
  }
}
