import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDivisionCompetidor, DivisionCompetidor } from '../division-competidor.model';
import { DivisionCompetidorService } from '../service/division-competidor.service';
import { IDivision } from 'app/entities/division/division.model';
import { DivisionService } from 'app/entities/division/service/division.service';
import { ICompetidor } from 'app/entities/competidor/competidor.model';
import { CompetidorService } from 'app/entities/competidor/service/competidor.service';

@Component({
  selector: 'jhi-division-competidor-update',
  templateUrl: './division-competidor-update.component.html',
})
export class DivisionCompetidorUpdateComponent implements OnInit {
  isSaving = false;

  divisionsSharedCollection: IDivision[] = [];
  competidorsSharedCollection: ICompetidor[] = [];

  editForm = this.fb.group({
    id: [],
    idDivision: [null, [Validators.required]],
    idCompetidor: [null, [Validators.required]],
    division: [],
    competidor: [],
  });

  constructor(
    protected divisionCompetidorService: DivisionCompetidorService,
    protected divisionService: DivisionService,
    protected competidorService: CompetidorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ divisionCompetidor }) => {
      this.updateForm(divisionCompetidor);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const divisionCompetidor = this.createFromForm();
    if (divisionCompetidor.id !== undefined) {
      this.subscribeToSaveResponse(this.divisionCompetidorService.update(divisionCompetidor));
    } else {
      this.subscribeToSaveResponse(this.divisionCompetidorService.create(divisionCompetidor));
    }
  }

  trackDivisionById(_index: number, item: IDivision): number {
    return item.id!;
  }

  trackCompetidorById(_index: number, item: ICompetidor): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDivisionCompetidor>>): void {
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

  protected updateForm(divisionCompetidor: IDivisionCompetidor): void {
    this.editForm.patchValue({
      id: divisionCompetidor.id,
      idDivision: divisionCompetidor.idDivision,
      idCompetidor: divisionCompetidor.idCompetidor,
      division: divisionCompetidor.division,
      competidor: divisionCompetidor.competidor,
    });

    this.divisionsSharedCollection = this.divisionService.addDivisionToCollectionIfMissing(
      this.divisionsSharedCollection,
      divisionCompetidor.division
    );
    this.competidorsSharedCollection = this.competidorService.addCompetidorToCollectionIfMissing(
      this.competidorsSharedCollection,
      divisionCompetidor.competidor
    );
  }

  protected loadRelationshipsOptions(): void {
    this.divisionService
      .query()
      .pipe(map((res: HttpResponse<IDivision[]>) => res.body ?? []))
      .pipe(
        map((divisions: IDivision[]) =>
          this.divisionService.addDivisionToCollectionIfMissing(divisions, this.editForm.get('division')!.value)
        )
      )
      .subscribe((divisions: IDivision[]) => (this.divisionsSharedCollection = divisions));

    this.competidorService
      .query()
      .pipe(map((res: HttpResponse<ICompetidor[]>) => res.body ?? []))
      .pipe(
        map((competidors: ICompetidor[]) =>
          this.competidorService.addCompetidorToCollectionIfMissing(competidors, this.editForm.get('competidor')!.value)
        )
      )
      .subscribe((competidors: ICompetidor[]) => (this.competidorsSharedCollection = competidors));
  }

  protected createFromForm(): IDivisionCompetidor {
    return {
      ...new DivisionCompetidor(),
      id: this.editForm.get(['id'])!.value,
      idDivision: this.editForm.get(['idDivision'])!.value,
      idCompetidor: this.editForm.get(['idCompetidor'])!.value,
      division: this.editForm.get(['division'])!.value,
      competidor: this.editForm.get(['competidor'])!.value,
    };
  }
}
