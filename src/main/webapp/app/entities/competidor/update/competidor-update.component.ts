import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { ICompetidor, Competidor } from '../competidor.model';
import { CompetidorService } from '../service/competidor.service';

@Component({
  selector: 'jhi-competidor-update',
  templateUrl: './competidor-update.component.html',
})
export class CompetidorUpdateComponent implements OnInit {
  isSaving = false;

  selectedFile: any;
  objectURL: any;

  editForm = this.fb.group({
    id: [],
    nombre: [null, [Validators.required, Validators.maxLength(50)]],
    foto: [null, [Validators.required, Validators.maxLength(250)]],
  });

  constructor(
    protected competidorService: CompetidorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ competidor }) => {
      this.updateForm(competidor);
    });
  }

  onFileSelected(e: any): void {
    this.objectURL = URL.createObjectURL(<File>e.target.files[0]);
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const competidor = this.createFromForm();
    competidor.foto = this.objectURL;
    competidor.foto = 'src/main/java/cr/ac/cenfotec/appostado/temp/PSG.png';
    console.log(competidor.foto);

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
