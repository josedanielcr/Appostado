import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IProducto, Producto } from '../producto.model';
import { ProductoService } from '../service/producto.service';
import { AzureBlobStorageService } from '../../../services/azure-blob-storage/azure-blob-storage.service';

@Component({
  selector: 'jhi-producto-update',
  templateUrl: './producto-update.component.html',
})
export class ProductoUpdateComponent implements OnInit {
  isSaving = false;
  imagen: any;
  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.maxLength(100)]],
    descripcion: [null, [Validators.required, Validators.maxLength(200)]],
    costo: [null, [Validators.required]],
    foto: [null, [Validators.required, Validators.maxLength(200)]],
    codigoFijo: [null, [Validators.maxLength(50)]],
    numCompras: [],
  });

  constructor(
    protected imagenService: AzureBlobStorageService,
    protected productoService: ProductoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ producto }) => {
      this.updateForm(producto);
    });
  }

  previousState(): void {
    window.history.back();
  }
  onFileSelected(evento: any): void {
    this.imagen = evento.target.files[0];
  }
  save(): void {
    this.isSaving = true;
    const producto = this.createFromForm();
    const nameImagen = this.editForm.get(['nombre'])!.value.concat(this.imagen.name);
    producto.foto = this.imagenService.createBlobInContainer(this.imagen, nameImagen);
    producto.numCompras = 0;
    if (producto.id !== undefined) {
      this.subscribeToSaveResponse(this.productoService.update(producto));
    } else {
      this.subscribeToSaveResponse(this.productoService.create(producto));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProducto>>): void {
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

  protected updateForm(producto: IProducto): void {
    this.editForm.patchValue({
      id: producto.id,
      nombre: producto.nombre,
      descripcion: producto.descripcion,
      costo: producto.costo,
      foto: producto.foto,
      codigoFijo: producto.codigoFijo,
      numCompras: producto.numCompras,
    });
  }

  protected createFromForm(): IProducto {
    return {
      ...new Producto(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      costo: this.editForm.get(['costo'])!.value,
      foto: this.editForm.get(['foto'])!.value,
      codigoFijo: this.editForm.get(['codigoFijo'])!.value,
      numCompras: this.editForm.get(['numCompras'])!.value,
    };
  }
}
