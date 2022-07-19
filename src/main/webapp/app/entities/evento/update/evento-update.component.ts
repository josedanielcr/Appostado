import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IEvento, Evento } from '../evento.model';
import { EventoService } from '../service/evento.service';
import { ICompetidor } from 'app/entities/competidor/competidor.model';
import { CompetidorService } from 'app/entities/competidor/service/competidor.service';
import { IDeporte } from 'app/entities/deporte/deporte.model';
import { DeporteService } from 'app/entities/deporte/service/deporte.service';
import { IDivision } from 'app/entities/division/division.model';
import { DivisionService } from 'app/entities/division/service/division.service';
import { IQuiniela } from 'app/entities/quiniela/quiniela.model';
import { QuinielaService } from 'app/entities/quiniela/service/quiniela.service';

@Component({
  selector: 'jhi-evento-update',
  templateUrl: './evento-update.component.html',
})
export class EventoUpdateComponent implements OnInit {
  isSaving = false;

  competidorsSharedCollection: ICompetidor[] = [];
  deportesSharedCollection: IDeporte[] = [];
  divisionsSharedCollection: IDivision[] = [];
  quinielasSharedCollection: IQuiniela[] = [];

  editForm = this.fb.group({
    id: [],
    marcador1: [],
    marcador2: [],
    estado: [null, [Validators.required, Validators.maxLength(20)]],
    multiplicador: [null, [Validators.required]],
    fecha: [null, [Validators.required]],
    horaInicio: [null, [Validators.required]],
    horaFin: [null, [Validators.required]],
    ganador: [],
    deporte: [],
    division: [],
    competidor1: [],
    competidor2: [],
    quiniela: [],
  });

  constructor(
    protected eventoService: EventoService,
    protected competidorService: CompetidorService,
    protected deporteService: DeporteService,
    protected divisionService: DivisionService,
    protected quinielaService: QuinielaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evento }) => {
      if (evento.id === undefined) {
        const today = dayjs().startOf('day');
        evento.horaInicio = today;
        evento.horaFin = today;
      }

      this.updateForm(evento);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const evento = this.createFromForm();
    if (evento.id !== undefined) {
      this.subscribeToSaveResponse(this.eventoService.update(evento));
    } else {
      this.subscribeToSaveResponse(this.eventoService.create(evento));
    }
  }

  trackCompetidorById(_index: number, item: ICompetidor): number {
    return item.id!;
  }

  trackDeporteById(_index: number, item: IDeporte): number {
    return item.id!;
  }

  trackDivisionById(_index: number, item: IDivision): number {
    return item.id!;
  }

  trackQuinielaById(_index: number, item: IQuiniela): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvento>>): void {
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

  protected updateForm(evento: IEvento): void {
    this.editForm.patchValue({
      id: evento.id,
      marcador1: evento.marcador1,
      marcador2: evento.marcador2,
      estado: evento.estado,
      multiplicador: evento.multiplicador,
      fecha: evento.fecha,
      horaInicio: evento.horaInicio ? evento.horaInicio.format(DATE_TIME_FORMAT) : null,
      horaFin: evento.horaFin ? evento.horaFin.format(DATE_TIME_FORMAT) : null,
      ganador: evento.ganador,
      deporte: evento.deporte,
      division: evento.division,
      competidor1: evento.competidor1,
      competidor2: evento.competidor2,
      quiniela: evento.quiniela,
    });

    this.competidorsSharedCollection = this.competidorService.addCompetidorToCollectionIfMissing(
      this.competidorsSharedCollection,
      evento.ganador,
      evento.competidor1,
      evento.competidor2
    );
    this.deportesSharedCollection = this.deporteService.addDeporteToCollectionIfMissing(this.deportesSharedCollection, evento.deporte);
    this.divisionsSharedCollection = this.divisionService.addDivisionToCollectionIfMissing(this.divisionsSharedCollection, evento.division);
    this.quinielasSharedCollection = this.quinielaService.addQuinielaToCollectionIfMissing(this.quinielasSharedCollection, evento.quiniela);
  }

  protected loadRelationshipsOptions(): void {
    this.competidorService
      .query()
      .pipe(map((res: HttpResponse<ICompetidor[]>) => res.body ?? []))
      .pipe(
        map((competidors: ICompetidor[]) =>
          this.competidorService.addCompetidorToCollectionIfMissing(
            competidors,
            this.editForm.get('ganador')!.value,
            this.editForm.get('competidor1')!.value,
            this.editForm.get('competidor2')!.value
          )
        )
      )
      .subscribe((competidors: ICompetidor[]) => (this.competidorsSharedCollection = competidors));

    this.deporteService
      .query()
      .pipe(map((res: HttpResponse<IDeporte[]>) => res.body ?? []))
      .pipe(
        map((deportes: IDeporte[]) => this.deporteService.addDeporteToCollectionIfMissing(deportes, this.editForm.get('deporte')!.value))
      )
      .subscribe((deportes: IDeporte[]) => (this.deportesSharedCollection = deportes));

    this.divisionService
      .query()
      .pipe(map((res: HttpResponse<IDivision[]>) => res.body ?? []))
      .pipe(
        map((divisions: IDivision[]) =>
          this.divisionService.addDivisionToCollectionIfMissing(divisions, this.editForm.get('division')!.value)
        )
      )
      .subscribe((divisions: IDivision[]) => (this.divisionsSharedCollection = divisions));

    this.quinielaService
      .query()
      .pipe(map((res: HttpResponse<IQuiniela[]>) => res.body ?? []))
      .pipe(
        map((quinielas: IQuiniela[]) =>
          this.quinielaService.addQuinielaToCollectionIfMissing(quinielas, this.editForm.get('quiniela')!.value)
        )
      )
      .subscribe((quinielas: IQuiniela[]) => (this.quinielasSharedCollection = quinielas));
  }

  protected createFromForm(): IEvento {
    return {
      ...new Evento(),
      id: this.editForm.get(['id'])!.value,
      marcador1: this.editForm.get(['marcador1'])!.value,
      marcador2: this.editForm.get(['marcador2'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      multiplicador: this.editForm.get(['multiplicador'])!.value,
      fecha: this.editForm.get(['fecha'])!.value,
      horaInicio: this.editForm.get(['horaInicio'])!.value ? dayjs(this.editForm.get(['horaInicio'])!.value, DATE_TIME_FORMAT) : undefined,
      horaFin: this.editForm.get(['horaFin'])!.value ? dayjs(this.editForm.get(['horaFin'])!.value, DATE_TIME_FORMAT) : undefined,
      ganador: this.editForm.get(['ganador'])!.value,
      deporte: this.editForm.get(['deporte'])!.value,
      division: this.editForm.get(['division'])!.value,
      competidor1: this.editForm.get(['competidor1'])!.value,
      competidor2: this.editForm.get(['competidor2'])!.value,
      quiniela: this.editForm.get(['quiniela'])!.value,
    };
  }
}
