import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProductoUsuario, ProductoUsuario } from '../producto-usuario.model';
import { ProductoUsuarioService } from '../service/producto-usuario.service';
import { IProducto } from 'app/entities/producto/producto.model';
import { ProductoService } from 'app/entities/producto/service/producto.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

@Component({
  selector: 'jhi-producto-usuario-update',
  templateUrl: './producto-usuario-update.component.html',
})
export class ProductoUsuarioUpdateComponent implements OnInit {
  isSaving = false;

  productosSharedCollection: IProducto[] = [];
  usuariosSharedCollection: IUsuario[] = [];

  editForm = this.fb.group({
    id: [],
    reclamado: [null, [Validators.required]],
    codigo: [null, [Validators.required, Validators.maxLength(50)]],
    producto: [],
    usuario: [],
  });

  constructor(
    protected productoUsuarioService: ProductoUsuarioService,
    protected productoService: ProductoService,
    protected usuarioService: UsuarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productoUsuario }) => {
      this.updateForm(productoUsuario);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productoUsuario = this.createFromForm();
    if (productoUsuario.id !== undefined) {
      this.subscribeToSaveResponse(this.productoUsuarioService.update(productoUsuario));
    } else {
      this.subscribeToSaveResponse(this.productoUsuarioService.create(productoUsuario));
    }
  }

  trackProductoById(_index: number, item: IProducto): number {
    return item.id!;
  }

  trackUsuarioById(_index: number, item: IUsuario): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductoUsuario>>): void {
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

  protected updateForm(productoUsuario: IProductoUsuario): void {
    this.editForm.patchValue({
      id: productoUsuario.id,
      reclamado: productoUsuario.reclamado,
      codigo: productoUsuario.codigo,
      producto: productoUsuario.producto,
      usuario: productoUsuario.usuario,
    });

    this.productosSharedCollection = this.productoService.addProductoToCollectionIfMissing(
      this.productosSharedCollection,
      productoUsuario.producto
    );
    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(
      this.usuariosSharedCollection,
      productoUsuario.usuario
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productoService
      .query()
      .pipe(map((res: HttpResponse<IProducto[]>) => res.body ?? []))
      .pipe(
        map((productos: IProducto[]) =>
          this.productoService.addProductoToCollectionIfMissing(productos, this.editForm.get('producto')!.value)
        )
      )
      .subscribe((productos: IProducto[]) => (this.productosSharedCollection = productos));

    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) => this.usuarioService.addUsuarioToCollectionIfMissing(usuarios, this.editForm.get('usuario')!.value))
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));
  }

  protected createFromForm(): IProductoUsuario {
    return {
      ...new ProductoUsuario(),
      id: this.editForm.get(['id'])!.value,
      reclamado: this.editForm.get(['reclamado'])!.value,
      codigo: this.editForm.get(['codigo'])!.value,
      producto: this.editForm.get(['producto'])!.value,
      usuario: this.editForm.get(['usuario'])!.value,
    };
  }
}
