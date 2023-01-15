import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { IUsuario, Usuario } from '../usuario.model';
import { UsuarioService } from '../service/usuario.service';
import { IUser, User } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { AzureBlobStorageService } from '../../../services/azure-blob-storage/azure-blob-storage.service';
import { countries } from '../../../account/register/country-data-store';

@Component({
  selector: 'jhi-usuario-update',
  templateUrl: './usuario-update.component.html',
})
export class UsuarioUpdateComponent implements OnInit {
  isSaving = false;
  imagen: any;
  user: IUser;
  public countryCollection: any = countries;

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    nombrePerfil: [null, [Validators.maxLength(100)]],
    pais: [null, [Validators.required, Validators.maxLength(100)]],
    fechaNacimiento: [null, [Validators.required]],
    imagenUrl: [null, []],
  });

  constructor(
    protected usuarioService: UsuarioService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected imagenService: AzureBlobStorageService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ usuario }) => {
      this.updateForm(usuario);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const usuario = this.createFromForm();
    if (this.imagen != null) {
      const nameImagen = this.editForm.get(['nombrePerfil'])!.value.concat(this.imagen.name);
      const obj = new User(this.editForm.get(['id'])!.value, 'login', this.imagenService.createBlobInContainer(this.imagen, nameImagen));
      usuario.user = obj;
    }
    if (usuario.id !== undefined) {
      this.subscribeToSaveResponse(this.usuarioService.update(usuario));
    } else {
      this.subscribeToSaveResponse(this.usuarioService.create(usuario));
    }
  }

  onFileSelected(evento: any): void {
    this.imagen = evento.target.files[0];
  }

  trackUserById(_index: number, item: IUser): number {
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
      nombrePerfil: usuario.nombrePerfil,
      pais: usuario.pais,
      fechaNacimiento: usuario.fechaNacimiento,
      user: usuario.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, usuario.user);
  }

  protected createFromForm(): IUsuario {
    return {
      ...new Usuario(),
      id: this.editForm.get(['id'])!.value,
      nombrePerfil: this.editForm.get(['nombrePerfil'])!.value,
      pais: this.editForm.get(['pais'])!.value,
      fechaNacimiento: this.editForm.get(['fechaNacimiento'])!.value,
    };
  }
}
