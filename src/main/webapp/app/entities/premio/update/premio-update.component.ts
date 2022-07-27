import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPremio, Premio } from '../premio.model';
import { PremioService } from '../service/premio.service';
import { AzureBlobStorageService } from '../../../services/azure-blob-storage/azure-blob-storage.service';

@Component({
  selector: 'jhi-premio-update',
  templateUrl: './premio-update.component.html',
})
export class PremioUpdateComponent implements OnInit {
  isSaving = false;
  imagen: any;
  objectURL: any;
  public estados: any = [{ estado: 'Activo' }, { estado: 'Inactivo' }];
  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.maxLength(100)]],
    descripcion: [null, [Validators.required, Validators.maxLength(250)]],
    foto: [null, [Validators.maxLength(250)]],
    costo: [null, [Validators.required]],
    estado: [null, [Validators.required, Validators.maxLength(20)]],
    stock: [],
    numCanjes: [],
  });

  constructor(
    protected imagenService: AzureBlobStorageService,
    protected premioService: PremioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ premio }) => {
      this.updateForm(premio);
    });
  }

  onFileSelected(evento: any): void {
    this.imagen = evento.target.files[0];
  }
  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const premio = this.createFromForm();
    premio.foto = this.objectURL;
    const nameImagen = this.editForm.get(['nombre'])!.value.concat(this.imagen.name);
    premio.foto = this.imagenService.createBlobInContainer(this.imagen, nameImagen);
    premio.numCanjes = 0;

    if (premio.id !== undefined) {
      this.subscribeToSaveResponse(this.premioService.update(premio));
    } else {
      this.subscribeToSaveResponse(this.premioService.create(premio));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPremio>>): void {
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

  protected updateForm(premio: IPremio): void {
    this.editForm.patchValue({
      id: premio.id,
      nombre: premio.nombre,
      descripcion: premio.descripcion,
      foto: premio.foto,
      costo: premio.costo,
      estado: premio.estado,
      stock: premio.stock,
      numCanjes: premio.numCanjes,
    });
  }

  protected createFromForm(): IPremio {
    return {
      ...new Premio(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      foto: this.editForm.get(['foto'])!.value,
      costo: this.editForm.get(['costo'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      stock: this.editForm.get(['stock'])!.value,
      numCanjes: this.editForm.get(['numCanjes'])!.value,
    };
  }
}
