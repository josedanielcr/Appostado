import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { INotificacion, Notificacion } from '../notificacion.model';
import { NotificacionService } from '../service/notificacion.service';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { UsuarioService } from 'app/entities/usuario/service/usuario.service';

@Component({
  selector: 'jhi-notificacion-update',
  templateUrl: './notificacion-update.component.html',
})
export class NotificacionUpdateComponent implements OnInit {
  isSaving = false;

  usuariosSharedCollection: IUsuario[] = [];

  editForm = this.fb.group({
    id: [],
    descripcion: [null, [Validators.required, Validators.maxLength(100)]],
    tipo: [null, [Validators.required, Validators.maxLength(20)]],
    fecha: [null, [Validators.required]],
    haGanado: [],
    ganancia: [],
    fueLeida: [],
    usuario: [],
  });

  constructor(
    protected notificacionService: NotificacionService,
    protected usuarioService: UsuarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notificacion }) => {
      this.updateForm(notificacion);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const notificacion = this.createFromForm();
    if (notificacion.id !== undefined) {
      this.subscribeToSaveResponse(this.notificacionService.update(notificacion));
    } else {
      this.subscribeToSaveResponse(this.notificacionService.create(notificacion));
    }
  }

  trackUsuarioById(_index: number, item: IUsuario): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotificacion>>): void {
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

  protected updateForm(notificacion: INotificacion): void {
    this.editForm.patchValue({
      id: notificacion.id,
      descripcion: notificacion.descripcion,
      tipo: notificacion.tipo,
      fecha: notificacion.fecha,
      haGanado: notificacion.haGanado,
      ganancia: notificacion.ganancia,
      fueLeida: notificacion.fueLeida,
      usuario: notificacion.usuario,
    });

    this.usuariosSharedCollection = this.usuarioService.addUsuarioToCollectionIfMissing(
      this.usuariosSharedCollection,
      notificacion.usuario
    );
  }

  protected loadRelationshipsOptions(): void {
    this.usuarioService
      .query()
      .pipe(map((res: HttpResponse<IUsuario[]>) => res.body ?? []))
      .pipe(
        map((usuarios: IUsuario[]) => this.usuarioService.addUsuarioToCollectionIfMissing(usuarios, this.editForm.get('usuario')!.value))
      )
      .subscribe((usuarios: IUsuario[]) => (this.usuariosSharedCollection = usuarios));
  }

  protected createFromForm(): INotificacion {
    return {
      ...new Notificacion(),
      id: this.editForm.get(['id'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
      fecha: this.editForm.get(['fecha'])!.value,
      haGanado: this.editForm.get(['haGanado'])!.value,
      ganancia: this.editForm.get(['ganancia'])!.value,
      fueLeida: this.editForm.get(['fueLeida'])!.value,
      usuario: this.editForm.get(['usuario'])!.value,
    };
  }
}
