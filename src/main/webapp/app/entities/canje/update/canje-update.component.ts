import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICanje, Canje } from '../canje.model';
import { CanjeService } from '../service/canje.service';
import { IPremio } from 'app/entities/premio/premio.model';
import { PremioService } from 'app/entities/premio/service/premio.service';
import { ITransaccion } from 'app/entities/transaccion/transaccion.model';
import { TransaccionService } from 'app/entities/transaccion/service/transaccion.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'jhi-canje-update',
  templateUrl: './canje-update.component.html',
})
export class CanjeUpdateComponent implements OnInit {
  isSaving = false;

  premiosSharedCollection: IPremio[] = [];
  transaccionsSharedCollection: ITransaccion[] = [];

  editForm = this.fb.group({
    id: [],
    estado: [null, [Validators.maxLength(20)]],
    detalle: [null, [Validators.maxLength(500)]],
    premio: [],
    transaccion: [],
  });

  respuesta = '';

  constructor(
    protected canjeService: CanjeService,
    protected premioService: PremioService,
    protected transaccionService: TransaccionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ canje }) => {
      this.updateForm(canje);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const canje = this.createFromForm();
    if (canje.id !== undefined) {
      this.subscribeToSaveResponse(this.canjeService.update(canje));
      Swal.fire({
        icon: 'question',
        title: 'Estás seguro de que deseas realizar este canje?',
        showDenyButton: true,
        confirmButtonColor: '#38b000',
        confirmButtonText: `Si`,
        denyButtonText: `No`,
      }).then(result => {
        /* Read more about isConfirmed, isDenied below */
        if (result.isConfirmed) {
          this.canjeService.completarCanje(canje.transaccion!, canje).subscribe(
            data => {
              this.respuesta = data;
              if (this.respuesta === 'si') {
                Swal.fire({
                  icon: 'success',
                  title: 'Canje completado',
                  confirmButtonColor: '#38b000',
                });
              } else {
                Swal.fire({
                  icon: 'error',
                  title: 'Oopss...',
                  text: 'No se pudo completar el canje.',
                  confirmButtonColor: '#38b000',
                  timer: 10000,
                });
              }
            },
            error => {
              console.log(error);
            }
          );
        } else if (result.isDenied) {
          canje.estado = 'Pendiente';
          this.subscribeToSaveResponse(this.canjeService.update(canje));
          window.location.reload();
          Swal.fire({
            icon: 'error',
            title: 'Canje cancelado',
            confirmButtonColor: '#38b000',
            timer: 10000,
          });
        }
      });
    } else {
      this.subscribeToSaveResponse(this.canjeService.create(canje));
    }
  }

  trackPremioById(_index: number, item: IPremio): number {
    return item.id!;
  }

  trackTransaccionById(_index: number, item: ITransaccion): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICanje>>): void {
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

  protected updateForm(canje: ICanje): void {
    this.editForm.patchValue({
      id: canje.id,
      estado: 'Completado',
      detalle: canje.detalle,
      premio: canje.premio,
      transaccion: canje.transaccion,
    });

    this.premiosSharedCollection = this.premioService.addPremioToCollectionIfMissing(this.premiosSharedCollection, canje.premio);
    this.transaccionsSharedCollection = this.transaccionService.addTransaccionToCollectionIfMissing(
      this.transaccionsSharedCollection,
      canje.transaccion
    );
  }

  protected loadRelationshipsOptions(): void {
    this.premioService
      .query()
      .pipe(map((res: HttpResponse<IPremio[]>) => res.body ?? []))
      .pipe(map((premios: IPremio[]) => this.premioService.addPremioToCollectionIfMissing(premios, this.editForm.get('premio')!.value)))
      .subscribe((premios: IPremio[]) => (this.premiosSharedCollection = premios));

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

  protected createFromForm(): ICanje {
    return {
      ...new Canje(),
      id: this.editForm.get(['id'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      detalle: this.editForm.get(['detalle'])!.value,
      premio: this.editForm.get(['premio'])!.value,
      transaccion: this.editForm.get(['transaccion'])!.value,
    };
  }
}
