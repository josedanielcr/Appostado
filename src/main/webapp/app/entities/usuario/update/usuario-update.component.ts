import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IUsuario, Usuario } from '../usuario.model';
import { UsuarioService } from '../service/usuario.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ICuentaUsuario } from 'app/entities/cuenta-usuario/cuenta-usuario.model';
import { CuentaUsuarioService } from 'app/entities/cuenta-usuario/service/cuenta-usuario.service';

@Component({
  selector: 'jhi-usuario-update',
  templateUrl: './usuario-update.component.html',
})
export class UsuarioUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  cuentasCollection: ICuentaUsuario[] = [];

  editForm = this.fb.group({
    id: [],
    idCuenta: [null, [Validators.required]],
    nombreUsuario: [null, [Validators.required, Validators.maxLength(100)]],
    nombrePerfil: [null, [Validators.maxLength(100)]],
    pais: [null, [Validators.required, Validators.maxLength(100)]],
    fechaNacimiento: [null, [Validators.required]],
    user: [],
    cuenta: [],
  });

  constructor(
    protected usuarioService: UsuarioService,
    protected userService: UserService,
    protected cuentaUsuarioService: CuentaUsuarioService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usuario }) => {
      this.updateForm(usuario);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const usuario = this.createFromForm();
    if (usuario.id !== undefined) {
      this.subscribeToSaveResponse(this.usuarioService.update(usuario));
    } else {
      this.subscribeToSaveResponse(this.usuarioService.create(usuario));
    }
  }

  trackUserById(_index: number, item: IUser): number {
    return item.id!;
  }

  trackCuentaUsuarioById(_index: number, item: ICuentaUsuario): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUsuario>>): void {
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

  protected updateForm(usuario: IUsuario): void {
    this.editForm.patchValue({
      id: usuario.id,
      idCuenta: usuario.idCuenta,
      nombreUsuario: usuario.nombreUsuario,
      nombrePerfil: usuario.nombrePerfil,
      pais: usuario.pais,
      fechaNacimiento: usuario.fechaNacimiento,
      user: usuario.user,
      cuenta: usuario.cuenta,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, usuario.user);
    this.cuentasCollection = this.cuentaUsuarioService.addCuentaUsuarioToCollectionIfMissing(this.cuentasCollection, usuario.cuenta);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.cuentaUsuarioService
      .query({ filter: 'usuario-is-null' })
      .pipe(map((res: HttpResponse<ICuentaUsuario[]>) => res.body ?? []))
      .pipe(
        map((cuentaUsuarios: ICuentaUsuario[]) =>
          this.cuentaUsuarioService.addCuentaUsuarioToCollectionIfMissing(cuentaUsuarios, this.editForm.get('cuenta')!.value)
        )
      )
      .subscribe((cuentaUsuarios: ICuentaUsuario[]) => (this.cuentasCollection = cuentaUsuarios));
  }

  protected createFromForm(): IUsuario {
    return {
      ...new Usuario(),
      id: this.editForm.get(['id'])!.value,
      idCuenta: this.editForm.get(['idCuenta'])!.value,
      nombreUsuario: this.editForm.get(['nombreUsuario'])!.value,
      nombrePerfil: this.editForm.get(['nombrePerfil'])!.value,
      pais: this.editForm.get(['pais'])!.value,
      fechaNacimiento: this.editForm.get(['fechaNacimiento'])!.value,
      user: this.editForm.get(['user'])!.value,
      cuenta: this.editForm.get(['cuenta'])!.value,
    };
  }
}
