import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAmigo, Amigo } from '../amigo.model';
import { AmigoService } from '../service/amigo.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

@Component({
  selector: 'jhi-amigo-update',
  templateUrl: './amigo-update.component.html',
})
export class AmigoUpdateComponent implements OnInit {
  isSaving = false;

  usuariosSharedCollection: IUsuario[] = [];

  editForm = this.fb.group({
    id: [],
    usuario: [],
    amigo: [],
  });

  constructor(
    protected amigoService: AmigoService,
    protected usuarioService: UsuarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ amigo }) => {
      this.updateForm(amigo);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const amigo = this.createFromForm();
    if (amigo.id !== undefined) {
      this.subscribeToSaveResponse(this.amigoService.update(amigo));
    } else {
      this.subscribeToSaveResponse(this.amigoService.create(amigo));
    }
  }

  trackUsuarioById(_index: number, item: IUsuario): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAmigo>>): void {
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

  protected updateForm(amigo: IAmigo): void {
    this.editForm.patchValue({
      id: amigo.id,
      usuario: amigo.usuario,
      amigo: amigo.amigo,
    });

    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(
      this.usuariosSharedCollection,
      amigo.usuario,
      amigo.amigo
    );
  }

  protected loadRelationshipsOptions(): void {
    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) =>
          this.usuarioService.addUsuarioToCollectionIfMissing(
            usuarios,
            this.editForm.get('usuario')!.value,
            this.editForm.get('amigo')!.value
          )
        )
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));
  }

  protected createFromForm(): IAmigo {
    return {
      ...new Amigo(),
      id: this.editForm.get(['id'])!.value,
      usuario: this.editForm.get(['usuario'])!.value,
      amigo: this.editForm.get(['amigo'])!.value,
    };
  }
}
