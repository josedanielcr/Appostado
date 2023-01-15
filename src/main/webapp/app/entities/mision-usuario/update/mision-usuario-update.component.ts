import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMisionUsuario, MisionUsuario } from '../mision-usuario.model';
import { MisionUsuarioService } from '../service/mision-usuario.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';
import { IMision } from 'app/entities/mision/mision.model';
import { MisionService } from 'app/entities/mision/service/mision.service';

@Component({
  selector: 'jhi-mision-usuario-update',
  templateUrl: './mision-usuario-update.component.html',
})
export class MisionUsuarioUpdateComponent implements OnInit {
  isSaving = false;

  usuariosSharedCollection: IUsuario[] = [];
  misionsSharedCollection: IMision[] = [];

  editForm = this.fb.group({
    id: [],
    completado: [null, [Validators.required]],
    usuario: [],
    mision: [],
  });

  constructor(
    protected misionUsuarioService: MisionUsuarioService,
    protected usuarioService: UsuarioService,
    protected misionService: MisionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ misionUsuario }) => {
      this.updateForm(misionUsuario);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const misionUsuario = this.createFromForm();
    if (misionUsuario.id !== undefined) {
      this.subscribeToSaveResponse(this.misionUsuarioService.update(misionUsuario));
    } else {
      this.subscribeToSaveResponse(this.misionUsuarioService.create(misionUsuario));
    }
  }

  trackUsuarioById(_index: number, item: IUsuario): number {
    return item.id!;
  }

  trackMisionById(_index: number, item: IMision): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMisionUsuario>>): void {
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

  protected updateForm(misionUsuario: IMisionUsuario): void {
    this.editForm.patchValue({
      id: misionUsuario.id,
      completado: misionUsuario.completado,
      usuario: misionUsuario.usuario,
      mision: misionUsuario.mision,
    });

    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(
      this.usuariosSharedCollection,
      misionUsuario.usuario
    );
    this.misionsSharedCollection = this.misionService.addMisionToCollectionIfMissing(this.misionsSharedCollection, misionUsuario.mision);
  }

  protected loadRelationshipsOptions(): void {
    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) => this.usuarioService.addUsuarioToCollectionIfMissing(usuarios, this.editForm.get('usuario')!.value))
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));

    this.misionService
      .query()
      .pipe(map((res: HttpResponse<IMision[]>) => res.body ?? []))
      .pipe(map((misions: IMision[]) => this.misionService.addMisionToCollectionIfMissing(misions, this.editForm.get('mision')!.value)))
      .subscribe((misions: IMision[]) => (this.misionsSharedCollection = misions));
  }

  protected createFromForm(): IMisionUsuario {
    return {
      ...new MisionUsuario(),
      id: this.editForm.get(['id'])!.value,
      completado: this.editForm.get(['completado'])!.value,
      usuario: this.editForm.get(['usuario'])!.value,
      mision: this.editForm.get(['mision'])!.value,
    };
  }
}
