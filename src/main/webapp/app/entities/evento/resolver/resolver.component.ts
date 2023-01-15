import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Evento, IEvento } from '../evento.model';
import { EventoService } from '../service/evento.service';
import { ActivatedRoute } from '@angular/router';
import { ICompetidor } from '../../competidor/competidor.model';
import { finalize, map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { CompetidorService } from '../../competidor/service/competidor.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'jhi-resolver',
  templateUrl: './resolver.component.html',
  styleUrls: ['./resolver.component.scss'],
})
export class ResolverComponent implements OnInit {
  evento: IEvento | null = null;
  idEvento: any;
  competidor1: ICompetidor | null = null;
  competidor2: ICompetidor | null = null;
  isSaving = false;
  competidorsSharedCollection: ICompetidor[] = [];

  editForm = this.fb.group({
    id: [],
    ganador: [],
    marcador1: [null, [Validators.required]],
    marcador2: [null, [Validators.required]],
  });

  constructor(
    protected fb: FormBuilder,
    protected activatedRoute: ActivatedRoute,
    protected competidorService: CompetidorService,
    protected eventoService: EventoService
  ) {
    return;
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evento }) => {
      this.evento = evento;
      this.idEvento = evento.id;
      this.loadRelationshipsOptions();
    });
  }

  trackCompetidorById(_index: number, item: ICompetidor): number {
    return item.id!;
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const eventoListo: IEvento = new Evento();
    eventoListo.id = this.idEvento;
    eventoListo.ganador = this.editForm.get(['ganador'])!.value;
    eventoListo.marcador1 = this.editForm.get(['marcador1'])!.value;
    eventoListo.marcador2 = this.editForm.get(['marcador2'])!.value;
    eventoListo.competidor1 = this.competidor1;
    eventoListo.competidor2 = this.competidor2;
    eventoListo.estado = this.evento?.estado;
    eventoListo.fecha = this.evento?.fecha;
    eventoListo.horaInicio = this.evento?.horaInicio;
    eventoListo.horaFin = this.evento?.horaFin;
    eventoListo.multiplicador = this.evento?.multiplicador;

    console.log(eventoListo);
    this.subscribeToSaveResponse(this.eventoService.resolver(eventoListo));
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

  protected loadRelationshipsOptions(): void {
    this.competidorService
      .getCompetidoresEvento(this.idEvento)
      .pipe(map((res: HttpResponse<ICompetidor[]>) => res.body ?? []))
      .pipe(
        map((competidors: ICompetidor[]) =>
          this.competidorService.addCompetidorToCollectionIfMissing(competidors, this.editForm.get('ganador')!.value)
        )
      )
      .subscribe(
        (competidors: ICompetidor[]) => (
          (this.competidorsSharedCollection = competidors),
          (this.competidor1 = this.competidorsSharedCollection[1]),
          (this.competidor2 = this.competidorsSharedCollection[2])
        )
      );
  }
}
