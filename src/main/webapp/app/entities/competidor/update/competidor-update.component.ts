import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { ICompetidor, Competidor } from '../competidor.model';
import { CompetidorService } from '../service/competidor.service';
import { AzureBlobStorageService } from '../../../services/azure-blob-storage/azure-blob-storage.service';

@Component({
  selector: 'jhi-competidor-update',
  templateUrl: './competidor-update.component.html',
})
export class CompetidorUpdateComponent implements OnInit {
  isSaving = false;
  imagen: File;

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.maxLength(50)]],
    foto: [null, [Validators.required, Validators.maxLength(250)]],
  });

  constructor(
    protected competidorService: CompetidorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected imagenService: AzureBlobStorageService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ competidor }) => {
      this.updateForm(competidor);
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
    const competidor = this.createFromForm();
    const nameImagen = this.editForm.get(['nombre'])!.value.concat(this.imagen.name);
    competidor.foto = this.imagenService.createBlobInContainer(this.imagen, nameImagen);
    if (competidor.id !== undefined) {
      this.subscribeToSaveResponse(this.competidorService.update(competidor));
    } else {
      this.subscribeToSaveResponse(this.competidorService.create(competidor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompetidor>>): void {
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

  protected updateForm(competidor: ICompetidor): void {
    this.editForm.patchValue({
      id: competidor.id,
      nombre: competidor.nombre,
      foto: competidor.foto,
    });
  }

  protected createFromForm(): ICompetidor {
    return {
      ...new Competidor(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      foto: this.editForm.get(['foto'])!.value,
    };
  }
}
