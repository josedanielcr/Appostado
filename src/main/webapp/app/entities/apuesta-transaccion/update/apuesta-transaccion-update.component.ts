import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IApuestaTransaccion, ApuestaTransaccion } from '../apuesta-transaccion.model';
import { ApuestaTransaccionService } from '../service/apuesta-transaccion.service';
import { IApuesta } from 'app/entities/apuesta/apuesta.model';
import { ApuestaService } from 'app/entities/apuesta/service/apuesta.service';
import { ITransaccion } from 'app/entities/transaccion/transaccion.model';
import { TransaccionService } from 'app/entities/transaccion/service/transaccion.service';

@Component({
  selector: 'jhi-apuesta-transaccion-update',
  templateUrl: './apuesta-transaccion-update.component.html',
})
export class ApuestaTransaccionUpdateComponent implements OnInit {
  isSaving = false;

  apuestasSharedCollection: IApuesta[] = [];
  transaccionsSharedCollection: ITransaccion[] = [];

  editForm = this.fb.group({
    id: [],
    apuesta: [],
    transaccion: [],
  });

  constructor(
    protected apuestaTransaccionService: ApuestaTransaccionService,
    protected apuestaService: ApuestaService,
    protected transaccionService: TransaccionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ apuestaTransaccion }) => {
      this.updateForm(apuestaTransaccion);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const apuestaTransaccion = this.createFromForm();
    if (apuestaTransaccion.id !== undefined) {
      this.subscribeToSaveResponse(this.apuestaTransaccionService.update(apuestaTransaccion));
    } else {
      this.subscribeToSaveResponse(this.apuestaTransaccionService.create(apuestaTransaccion));
    }
  }

  trackApuestaById(_index: number, item: IApuesta): number {
    return item.id!;
  }

  trackTransaccionById(_index: number, item: ITransaccion): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApuestaTransaccion>>): void {
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

  protected updateForm(apuestaTransaccion: IApuestaTransaccion): void {
    this.editForm.patchValue({
      id: apuestaTransaccion.id,
      apuesta: apuestaTransaccion.apuesta,
      transaccion: apuestaTransaccion.transaccion,
    });

    this.apuestasSharedCollection = this.apuestaService.addApuestaToCollectionIfMissing(
      this.apuestasSharedCollection,
      apuestaTransaccion.apuesta
    );
    this.transaccionsSharedCollection = this.transaccionService.addTransaccionToCollectionIfMissing(
      this.transaccionsSharedCollection,
      apuestaTransaccion.transaccion
    );
  }

  protected loadRelationshipsOptions(): void {
    this.apuestaService
      .query()
      .pipe(map((res: HttpResponse<IApuesta[]>) => res.body ?? []))
      .pipe(
        map((apuestas: IApuesta[]) => this.apuestaService.addApuestaToCollectionIfMissing(apuestas, this.editForm.get('apuesta')!.value))
      )
      .subscribe((apuestas: IApuesta[]) => (this.apuestasSharedCollection = apuestas));

    this.transaccionService
      .query()
      .pipe(map((res: HttpResponse<ITransaccion[]>) => res.body ?? []))
      .pipe(
        map((transaccions: ITransaccion[]) =>
          this.transaccionService.addTransaccionToCollectionIfMissing(transaccions, this.editForm.get('transaccion')!.value)
        )
      )
      .subscribe((transaccions: ITransaccion[]) => (this.transaccionsSharedCollection = transaccions));
  }

  protected createFromForm(): IApuestaTransaccion {
    return {
      ...new ApuestaTransaccion(),
      id: this.editForm.get(['id'])!.value,
      apuesta: this.editForm.get(['apuesta'])!.value,
      transaccion: this.editForm.get(['transaccion'])!.value,
    };
  }
}
