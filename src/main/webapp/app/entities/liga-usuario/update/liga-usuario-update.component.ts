import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ILigaUsuario, LigaUsuario } from '../liga-usuario.model';
import { LigaUsuarioService } from '../service/liga-usuario.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { ILiga } from 'app/entities/liga/liga.model';
import { LigaService } from 'app/entities/liga/service/liga.service';

@Component({
  selector: 'jhi-liga-usuario-update',
  templateUrl: './liga-usuario-update.component.html',
})
export class LigaUsuarioUpdateComponent implements OnInit {
  isSaving = false;

  usuariosSharedCollection: IUsuario[] = [];
  ligasSharedCollection: ILiga[] = [];

  editForm = this.fb.group({
    id: [],
    usuario: [],
    liga: [],
  });

  constructor(
    protected ligaUsuarioService: LigaUsuarioService,
    protected usuarioService: UsuarioService,
    protected ligaService: LigaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ligaUsuario }) => {
      this.updateForm(ligaUsuario);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ligaUsuario = this.createFromForm();
    if (ligaUsuario.id !== undefined) {
      this.subscribeToSaveResponse(this.ligaUsuarioService.update(ligaUsuario));
    } else {
      this.subscribeToSaveResponse(this.ligaUsuarioService.create(ligaUsuario));
    }
  }

  trackUsuarioById(_index: number, item: IUsuario): number {
    return item.id!;
  }

  trackLigaById(_index: number, item: ILiga): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILigaUsuario>>): void {
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

  protected updateForm(ligaUsuario: ILigaUsuario): void {
    this.editForm.patchValue({
      id: ligaUsuario.id,
      usuario: ligaUsuario.usuario,
      liga: ligaUsuario.liga,
    });

    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(this.usuariosSharedCollection, ligaUsuario.usuario);
    this.ligasSharedCollection = this.ligaService.addLigaToCollectionIfMissing(this.ligasSharedCollection, ligaUsuario.liga);
  }

  protected loadRelationshipsOptions(): void {
    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) => this.usuarioService.addUsuarioToCollectionIfMissing(usuarios, this.editForm.get('usuario')!.value))
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));

    this.ligaService
      .query()
      .pipe(map((res: HttpResponse<ILiga[]>) => res.body ?? []))
      .pipe(map((ligas: ILiga[]) => this.ligaService.addLigaToCollectionIfMissing(ligas, this.editForm.get('liga')!.value)))
      .subscribe((ligas: ILiga[]) => (this.ligasSharedCollection = ligas));
  }

  protected createFromForm(): ILigaUsuario {
    return {
      ...new LigaUsuario(),
      id: this.editForm.get(['id'])!.value,
      usuario: this.editForm.get(['usuario'])!.value,
      liga: this.editForm.get(['liga'])!.value,
    };
  }
}
