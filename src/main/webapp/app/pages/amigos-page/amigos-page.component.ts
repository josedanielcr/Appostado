import { Component, OnInit } from '@angular/core';
import { AmigoDetail, NotificacionAmigo } from './amigos-page.model';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { AmigosPageService } from './amigos-page.service';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'jhi-amigos-page',
  templateUrl: './amigos-page.component.html',
  styleUrls: ['./amigos-page.component.scss', '../../../assets/styles1.css'],
})
export class AmigosPageComponent implements OnInit {
  solicitudes?: NotificacionAmigo[];
  amigos?: AmigoDetail[];
  amigo?: AmigoDetail;
  loadingAmigos = false;
  loadingSolicitudes: Promise<boolean> | null = null;
  errorUserExists = false;
  notificationExists = false;
  notificationSuccess = false;
  amigoExists = false;
  error = false;
  whatsApp?: string;
  facebook?: string;

  friendForm = this.fb.group({
    newAmigo: [],
  });

  constructor(protected amigosPageService: AmigosPageService, protected fb: FormBuilder) {}

  loadAllSolicitudes(): void {
    this.amigosPageService.getNotificacionesAmigos().subscribe({
      next: (res: HttpResponse<NotificacionAmigo[]>) => {
        this.solicitudes = res.body ?? [];
        this.loadingSolicitudes = Promise.resolve(true);
      },
      error: () => {
        this.loadingSolicitudes = Promise.resolve(false);
      },
    });
  }

  loadAllAmigos(): void {
    this.loadingAmigos = true;

    this.amigosPageService.getAmigos().subscribe({
      next: (res: HttpResponse<AmigoDetail[]>) => {
        this.amigos = res.body ?? [];
        this.loadingAmigos = false;
      },
      error: () => {
        this.loadingAmigos = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAllSolicitudes();
    this.getSocialLinks();
    this.loadAllAmigos();
  }

  updateAll(): void {
    this.loadAllSolicitudes();
    this.loadAllAmigos();
  }

  reject(amigoNuevo: string): void {
    this.amigosPageService.rejectAmigo(amigoNuevo).subscribe({
      next: () => this.updateAll(),
      error: response => this.processError(response),
    });
  }

  resetErrors(): void {
    this.notificationSuccess = false;
    this.notificationExists = false;
    this.errorUserExists = false;
    this.error = false;
    this.amigoExists = false;
  }
  accept(amigoNuevo: string): void {
    this.resetErrors();
    this.amigosPageService.acceptAmigo(amigoNuevo).subscribe({
      next: () => this.updateAll(),
      error: response => this.processError(response),
    });
  }

  delete(amigo: string): void {
    this.resetErrors();
    this.amigosPageService.deleteAmigo(amigo).subscribe({
      next: () => this.updateAll(),
      error: response => this.processError(response),
    });
  }

  request(): void {
    this.resetErrors();
    const newFriend = this.getFriendFromForm();
    if (newFriend !== undefined && newFriend !== null) {
      this.amigosPageService.requestAmigo(newFriend).subscribe({
        next: () => (this.notificationSuccess = true),
        error: response => this.processError(response),
      });
    } else {
      console.log('new friend input is empty');
    }
  }

  getSocialLinks(): void {
    this.whatsApp = 'https://wa.me/?text=' + location.origin;
    this.facebook = 'https://www.facebook.com/sharer.php?u=' + location.origin;
  }

  protected getFriendFromForm(): any {
    return this.friendForm.get(['newAmigo'])!.value;
  }

  private processError(response: HttpErrorResponse): void {
    console.log(response);
    if (response.status === 400 && response.error.errorKey === 'notfound') {
      this.errorUserExists = true;
    } else if (response.status === 400 && response.error.errorKey === 'notificationexists') {
      this.notificationExists = true;
    } else if (response.status === 400 && response.error.errorKey === 'amigoexists') {
      this.amigoExists = true;
    } else {
      this.error = true;
    }
  }
}
