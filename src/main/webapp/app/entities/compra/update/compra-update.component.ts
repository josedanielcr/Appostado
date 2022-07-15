import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICompra, Compra } from '../compra.model';
import { CompraService } from '../service/compra.service';
import { ITransaccion } from 'app/entities/transaccion/transaccion.model';
import { TransaccionService } from 'app/entities/transaccion/service/transaccion.service';
import { IProducto } from 'app/entities/producto/producto.model';
import { ProductoService } from 'app/entities/producto/service/producto.service';

@Component({
  selector: 'jhi-compra-update',
  templateUrl: './compra-update.component.html',
})
export class CompraUpdateComponent implements OnInit {
  isSaving = false;

  transaccionsCollection: ITransaccion[] = [];
  productosSharedCollection: IProducto[] = [];

  editForm = this.fb.group({
    id: [],
    idProducto: [null, [Validators.required]],
    idTransaccion: [null, [Validators.required]],
    transaccion: [],
    producto: [],
  });

  constructor(
    protected compraService: CompraService,
    protected transaccionService: TransaccionService,
    protected productoService: ProductoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compra }) => {
      this.updateForm(compra);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const compra = this.createFromForm();
    if (compra.id !== undefined) {
      this.subscribeToSaveResponse(this.compraService.update(compra));
    } else {
      this.subscribeToSaveResponse(this.compraService.create(compra));
    }
  }

  trackTransaccionById(_index: number, item: ITransaccion): number {
    return item.id!;
  }

  trackProductoById(_index: number, item: IProducto): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompra>>): void {
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

  protected updateForm(compra: ICompra): void {
    this.editForm.patchValue({
      id: compra.id,
      idProducto: compra.idProducto,
      idTransaccion: compra.idTransaccion,
      transaccion: compra.transaccion,
      producto: compra.producto,
    });

    this.transaccionsCollection = this.transaccionService.addTransaccionToCollectionIfMissing(
      this.transaccionsCollection,
      compra.transaccion
    );
    this.productosSharedCollection = this.productoService.addProductoToCollectionIfMissing(this.productosSharedCollection, compra.producto);
  }

  protected loadRelationshipsOptions(): void {
    this.transaccionService
      .query({ filter: 'compra-is-null' })
      .pipe(map((res: HttpResponse<ITransaccion[]>) => res.body ?? []))
      .pipe(
        map((transaccions: ITransaccion[]) =>
          this.transaccionService.addTransaccionToCollectionIfMissing(transaccions, this.editForm.get('transaccion')!.value)
        )
      )
      .subscribe((transaccions: ITransaccion[]) => (this.transaccionsCollection = transaccions));

    this.productoService
      .query()
      .pipe(map((res: HttpResponse<IProducto[]>) => res.body ?? []))
      .pipe(
        map((productos: IProducto[]) =>
          this.productoService.addProductoToCollectionIfMissing(productos, this.editForm.get('producto')!.value)
        )
      )
      .subscribe((productos: IProducto[]) => (this.productosSharedCollection = productos));
  }

  protected createFromForm(): ICompra {
    return {
      ...new Compra(),
      id: this.editForm.get(['id'])!.value,
      idProducto: this.editForm.get(['idProducto'])!.value,
      idTransaccion: this.editForm.get(['idTransaccion'])!.value,
      transaccion: this.editForm.get(['transaccion'])!.value,
      producto: this.editForm.get(['producto'])!.value,
    };
  }
}
