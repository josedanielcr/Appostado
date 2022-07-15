import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ILiga, Liga } from '../liga.model';
import { LigaService } from '../service/liga.service';

@Component({
  selector: 'jhi-liga-update',
  templateUrl: './liga-update.component.html',
})
export class LigaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.maxLength(100)]],
    descripcion: [null, [Validators.maxLength(250)]],
    foto: [],
  });

  constructor(protected ligaService: LigaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ liga }) => {
      this.updateForm(liga);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const liga = this.createFromForm();
    if (liga.id !== undefined) {
      this.subscribeToSaveResponse(this.ligaService.update(liga));
    } else {
      this.subscribeToSaveResponse(this.ligaService.create(liga));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILiga>>): void {
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

  protected updateForm(liga: ILiga): void {
    this.editForm.patchValue({
      id: liga.id,
      nombre: liga.nombre,
      descripcion: liga.descripcion,
      foto: liga.foto,
    });
  }

  protected createFromForm(): ILiga {
    return {
      ...new Liga(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      foto: this.editForm.get(['foto'])!.value,
    };
  }
}
