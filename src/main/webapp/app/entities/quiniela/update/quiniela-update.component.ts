import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IQuiniela, Quiniela } from '../quiniela.model';
import { QuinielaService } from '../service/quiniela.service';

@Component({
  selector: 'jhi-quiniela-update',
  templateUrl: './quiniela-update.component.html',
})
export class QuinielaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.maxLength(100)]],
    descripcion: [null, [Validators.maxLength(100)]],
    costoParticipacion: [null, [Validators.required]],
    primerPremio: [null, [Validators.required]],
    segundoPremio: [null, [Validators.required]],
    tercerPremio: [null, [Validators.required]],
    estado: [null, [Validators.required, Validators.maxLength(20)]],
  });

  constructor(protected quinielaService: QuinielaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quiniela }) => {
      this.updateForm(quiniela);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const quiniela = this.createFromForm();
    if (quiniela.id !== undefined) {
      this.subscribeToSaveResponse(this.quinielaService.update(quiniela));
    } else {
      this.subscribeToSaveResponse(this.quinielaService.create(quiniela));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuiniela>>): void {
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

  protected updateForm(quiniela: IQuiniela): void {
    this.editForm.patchValue({
      id: quiniela.id,
      nombre: quiniela.nombre,
      descripcion: quiniela.descripcion,
      costoParticipacion: quiniela.costoParticipacion,
      primerPremio: quiniela.primerPremio,
      segundoPremio: quiniela.segundoPremio,
      tercerPremio: quiniela.tercerPremio,
      estado: quiniela.estado,
    });
  }

  protected createFromForm(): IQuiniela {
    return {
      ...new Quiniela(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      costoParticipacion: this.editForm.get(['costoParticipacion'])!.value,
      primerPremio: this.editForm.get(['primerPremio'])!.value,
      segundoPremio: this.editForm.get(['segundoPremio'])!.value,
      tercerPremio: this.editForm.get(['tercerPremio'])!.value,
      estado: this.editForm.get(['estado'])!.value,
    };
  }
}
