import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { User } from '../user-management.model';
import { UserManagementService } from '../service/user-management.service';
import { countries } from '../../../account/register/country-data-store';
import { AzureBlobStorageService } from '../../../services/azure-blob-storage/azure-blob-storage.service';

@Component({
  selector: 'jhi-user-mgmt-update',
  templateUrl: './user-management-update.component.html',
})
export class UserManagementUpdateComponent implements OnInit {
  user!: User;
  authorities: string[] = [];
  isSaving = false;
  imagen: any;
  public countryCollection: any = countries;

  editForm = this.fb.group({
    id: [],
    login: [
      '',
      [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(50),
        Validators.pattern('^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$'),
      ],
    ],
    email: ['', [Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    activated: [],
    authorities: [],
    pais: [],
    imagenUrl: [],
  });

  constructor(
    protected imagenService: AzureBlobStorageService,
    private userService: UserManagementService,
    private route: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.route.data.subscribe(({ user }) => {
      if (user) {
        this.user = user;
        if (this.user.id === undefined) {
          this.user.activated = true;
        }
        this.updateForm(user);
      }
    });
    this.userService.authorities().subscribe(authorities => (this.authorities = authorities));
  }

  onFileSelected(evento: any): void {
    this.imagen = evento.target.files[0];
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    this.updateUser(this.user);

    if (this.imagen != null) {
      const nameImagen = this.editForm.get(['login'])!.value.concat(this.imagen.name);
      this.user.imageUrl = this.imagenService.createBlobInContainer(this.imagen, nameImagen);
    }

    if (this.user.id !== undefined) {
      this.userService.update(this.user).subscribe({
        next: () => this.onSaveSuccess(),
        error: () => this.onSaveError(),
      });
    } else {
      this.userService.create(this.user).subscribe({
        next: () => this.onSaveSuccess(),
        error: () => this.onSaveError(),
      });
    }
  }

  private updateForm(user: User): void {
    this.editForm.patchValue({
      id: user.id,
      login: user.login,
      email: user.email,
      activated: user.activated,
      authorities: user.authorities,
      pais: user.pais,
      imageUrl: user.imageUrl,
    });
  }

  private updateUser(user: User): void {
    user.login = this.editForm.get(['login'])!.value;
    user.email = this.editForm.get(['email'])!.value;
    user.activated = this.editForm.get(['activated'])!.value;
    user.authorities = this.editForm.get(['authorities'])!.value;
    user.pais = this.editForm.get(['pais'])!.value;
  }

  private onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  private onSaveError(): void {
    this.isSaving = false;
  }
}
