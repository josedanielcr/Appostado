import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IParametro, Parametro } from '../parametro.model';
import { ParametroService } from '../service/parametro.service';

@Component({
  selector: 'jhi-parametro-update',
  templateUrl: './parametro-update.component.html',
})
export class ParametroUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    telefono: [null, [Validators.maxLength(20)]],
    correo: [null, [Validators.maxLength(320)]],
    direccion: [null, [Validators.maxLength(250)]],
  });

  constructor(protected parametroService: ParametroService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ parametro }) => {
      this.updateForm(parametro);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const parametro = this.createFromForm();
    if (parametro.id !== undefined) {
      this.subscribeToSaveResponse(this.parametroService.update(parametro));
    } else {
      this.subscribeToSaveResponse(this.parametroService.create(parametro));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParametro>>): void {
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

  protected updateForm(parametro: IParametro): void {
    this.editForm.patchValue({
      id: parametro.id,
      telefono: parametro.telefono,
      correo: parametro.correo,
      direccion: parametro.direccion,
    });
  }

  protected createFromForm(): IParametro {
    return {
      ...new Parametro(),
      id: this.editForm.get(['id'])!.value,
      telefono: this.editForm.get(['telefono'])!.value,
      correo: this.editForm.get(['correo'])!.value,
      direccion: this.editForm.get(['direccion'])!.value,
    };
  }
}
