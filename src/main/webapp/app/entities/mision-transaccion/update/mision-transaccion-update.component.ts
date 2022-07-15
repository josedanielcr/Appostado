import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMisionTransaccion, MisionTransaccion } from '../mision-transaccion.model';
import { MisionTransaccionService } from '../service/mision-transaccion.service';
import { IMision } from 'app/entities/mision/mision.model';
import { MisionService } from 'app/entities/mision/service/mision.service';
import { ITransaccion } from 'app/entities/transaccion/transaccion.model';
import { TransaccionService } from 'app/entities/transaccion/service/transaccion.service';

@Component({
  selector: 'jhi-mision-transaccion-update',
  templateUrl: './mision-transaccion-update.component.html',
})
export class MisionTransaccionUpdateComponent implements OnInit {
  isSaving = false;

  misionsSharedCollection: IMision[] = [];
  transaccionsSharedCollection: ITransaccion[] = [];

  editForm = this.fb.group({
    id: [],
    idMision: [null, [Validators.required]],
    idTransaccion: [null, [Validators.required]],
    mision: [],
    transaccion: [],
  });

  constructor(
    protected misionTransaccionService: MisionTransaccionService,
    protected misionService: MisionService,
    protected transaccionService: TransaccionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ misionTransaccion }) => {
      this.updateForm(misionTransaccion);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const misionTransaccion = this.createFromForm();
    if (misionTransaccion.id !== undefined) {
      this.subscribeToSaveResponse(this.misionTransaccionService.update(misionTransaccion));
    } else {
      this.subscribeToSaveResponse(this.misionTransaccionService.create(misionTransaccion));
    }
  }

  trackMisionById(_index: number, item: IMision): number {
    return item.id!;
  }

  trackTransaccionById(_index: number, item: ITransaccion): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMisionTransaccion>>): void {
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

  protected updateForm(misionTransaccion: IMisionTransaccion): void {
    this.editForm.patchValue({
      id: misionTransaccion.id,
      idMision: misionTransaccion.idMision,
      idTransaccion: misionTransaccion.idTransaccion,
      mision: misionTransaccion.mision,
      transaccion: misionTransaccion.transaccion,
    });

    this.misionsSharedCollection = this.misionService.addMisionToCollectionIfMissing(
      this.misionsSharedCollection,
      misionTransaccion.mision
    );
    this.transaccionsSharedCollection = this.transaccionService.addTransaccionToCollectionIfMissing(
      this.transaccionsSharedCollection,
      misionTransaccion.transaccion
    );
  }

  protected loadRelationshipsOptions(): void {
    this.misionService
      .query()
      .pipe(map((res: HttpResponse<IMision[]>) => res.body ?? []))
      .pipe(map((misions: IMision[]) => this.misionService.addMisionToCollectionIfMissing(misions, this.editForm.get('mision')!.value)))
      .subscribe((misions: IMision[]) => (this.misionsSharedCollection = misions));

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

  protected createFromForm(): IMisionTransaccion {
    return {
      ...new MisionTransaccion(),
      id: this.editForm.get(['id'])!.value,
      idMision: this.editForm.get(['idMision'])!.value,
      idTransaccion: this.editForm.get(['idTransaccion'])!.value,
      mision: this.editForm.get(['mision'])!.value,
      transaccion: this.editForm.get(['transaccion'])!.value,
    };
  }
}
