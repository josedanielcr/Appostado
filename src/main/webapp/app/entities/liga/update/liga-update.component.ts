import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { ILiga, Liga } from '../liga.model';
import { LigaService } from '../service/liga.service';
import { AzureBlobStorageService } from '../../../services/azure-blob-storage/azure-blob-storage.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'jhi-liga-update',
  templateUrl: './liga-update.component.html',
})
export class LigaUpdateComponent implements OnInit {
  isSaving = false;
  imagen: File;
  notificationSuccess = false;
  error = false;

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.maxLength(100)]],
    descripcion: [null, [Validators.maxLength(250)]],
    foto: [],
  });

  constructor(
    protected ligaService: LigaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected imagenService: AzureBlobStorageService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ liga }) => {
      if (liga) {
        liga.descripcion = liga.descripcion ? this.ligaService.splitDescripcion(liga.descripcion) : undefined;
      }
      this.updateForm(liga);
    });
  }

  previousState(): void {
    window.history.back();
  }

  onFileSelected(evento: any): void {
    this.imagen = evento.target.files[0];
  }

  resetErrors(): void {
    this.notificationSuccess = false;
    this.error = false;
  }

  save(): void {
    this.resetErrors();
    this.isSaving = true;
    const liga = this.createFromForm();
    const nameImagen = this.editForm.get(['nombre'])!.value.concat(this.imagen.name);
    liga.foto = this.imagenService.createBlobInContainer(this.imagen, nameImagen);
    if (liga.id !== undefined) {
      this.subscribeToSaveResponse(this.ligaService.update(liga));
    } else {
      this.subscribeToSaveResponse(this.ligaService.create(liga));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILiga>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => {
        this.onSaveSuccess();
        Swal.fire({
          title: 'Liga procesada',
          text: 'Agrega amigos a tu nueva liga para empezar a competir junto a ellos.',
          icon: 'success',
          timer: 2500,
          showConfirmButton: false,
        });
        setTimeout(() => {
          this.previousState();
        }, 3000);
      },
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.notificationSuccess = true;
  }

  protected onSaveError(): void {
    this.error = true;
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
