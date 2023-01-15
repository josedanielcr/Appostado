import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IApuesta, Apuesta } from '../apuesta.model';
import { ApuestaService } from '../service/apuesta.service';
import { ICompetidor } from 'app/entities/competidor/competidor.model';
import { CompetidorService } from 'app/entities/competidor/service/competidor.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { IEvento } from 'app/entities/evento/evento.model';
import { EventoService } from 'app/entities/evento/service/evento.service';

@Component({
  selector: 'jhi-apuesta-update',
  templateUrl: './apuesta-update.component.html',
})
export class ApuestaUpdateComponent implements OnInit {
  isSaving = false;

  apostadosCollection: ICompetidor[] = [];
  usuariosSharedCollection: IUsuario[] = [];
  eventosSharedCollection: IEvento[] = [];

  editForm = this.fb.group({
    id: [],
    creditosApostados: [null, [Validators.required]],
    haGanado: [],
    estado: [null, [Validators.required, Validators.maxLength(20)]],
    apostado: [],
    usuario: [],
    evento: [],
  });

  constructor(
    protected apuestaService: ApuestaService,
    protected competidorService: CompetidorService,
    protected usuarioService: UsuarioService,
    protected eventoService: EventoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ apuesta }) => {
      this.updateForm(apuesta);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const apuesta = this.createFromForm();
    if (apuesta.id !== undefined) {
      this.subscribeToSaveResponse(this.apuestaService.update(apuesta));
    } else {
      this.subscribeToSaveResponse(this.apuestaService.create(apuesta));
    }
  }

  trackCompetidorById(_index: number, item: ICompetidor): number {
    return item.id!;
  }

  trackUsuarioById(_index: number, item: IUsuario): number {
    return item.id!;
  }

  trackEventoById(_index: number, item: IEvento): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApuesta>>): void {
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

  protected updateForm(apuesta: IApuesta): void {
    this.editForm.patchValue({
      id: apuesta.id,
      creditosApostados: apuesta.creditosApostados,
      haGanado: apuesta.haGanado,
      estado: apuesta.estado,
      apostado: apuesta.apostado,
      usuario: apuesta.usuario,
      evento: apuesta.evento,
    });

    this.apostadosCollection = this.competidorService.addCompetidorToCollectionIfMissing(this.apostadosCollection, apuesta.apostado);
    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(this.usuariosSharedCollection, apuesta.usuario);
    this.eventosSharedCollection = this.eventoService.addEventoToCollectionIfMissing(this.eventosSharedCollection, apuesta.evento);
  }

  protected loadRelationshipsOptions(): void {
    this.competidorService
      .query({ filter: 'apuesta-is-null' })
      .pipe(map((res: HttpResponse<ICompetidor[]>) => res.body ?? []))
      .pipe(
        map((competidors: ICompetidor[]) =>
          this.competidorService.addCompetidorToCollectionIfMissing(competidors, this.editForm.get('apostado')!.value)
        )
      )
      .subscribe((competidors: ICompetidor[]) => (this.apostadosCollection = competidors));

    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) => this.usuarioService.addUsuarioToCollectionIfMissing(usuarios, this.editForm.get('usuario')!.value))
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));

    this.eventoService
      .query()
      .pipe(map((res: HttpResponse<IEvento[]>) => res.body ?? []))
      .pipe(map((eventos: IEvento[]) => this.eventoService.addEventoToCollectionIfMissing(eventos, this.editForm.get('evento')!.value)))
      .subscribe((eventos: IEvento[]) => (this.eventosSharedCollection = eventos));
  }

  protected createFromForm(): IApuesta {
    return {
      ...new Apuesta(),
      id: this.editForm.get(['id'])!.value,
      creditosApostados: this.editForm.get(['creditosApostados'])!.value,
      haGanado: this.editForm.get(['haGanado'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      apostado: this.editForm.get(['apostado'])!.value,
      usuario: this.editForm.get(['usuario'])!.value,
      evento: this.editForm.get(['evento'])!.value,
    };
  }
}
