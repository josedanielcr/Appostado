import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILiga } from '../liga.model';
import { LigaService } from '../service/liga.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IRanking } from '../../cuenta-usuario/ranking-model';
import { AmigoDetail } from '../../../pages/amigos-page/amigos-page.model';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'jhi-liga-detail',
  templateUrl: './liga-detail.component.html',
  styleUrls: ['./liga-detail.component.scss', '../../../../assets/styles1.css'],
})
export class LigaDetailComponent implements OnInit {
  liga: ILiga | null = null;
  authorization: boolean | null;
  loading = true;
  rankings?: IRanking[];
  amigos?: AmigoDetail[];
  amigoExists = false;
  addAmigoSuccess = false;
  errorUserExists = false;
  error = false;

  friendForm = this.fb.group({
    newAmigo: [],
  });

  constructor(protected activatedRoute: ActivatedRoute, protected ligaService: LigaService, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ liga }) => {
      if (liga) {
        liga.descripcion = liga.descripcion ? this.ligaService.splitDescripcion(liga.descripcion) : undefined;
      }
      this.liga = liga;
      this.getAuthorizationEditLiga(liga.id);
      this.getAvailableAmigos(liga.id);
      this.getRankingGlobal(liga.id);
    });
  }

  getRankingGlobal(id: number): void {
    this.ligaService.getRanking(id).subscribe({
      next: (res: HttpResponse<IRanking[]>) => {
        this.rankings = res.body ?? [];
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      },
    });
  }

  getAuthorizationEditLiga(id: number): void {
    this.ligaService.getEditAuthorization(id).subscribe({
      next: (res: HttpResponse<boolean>) => {
        this.authorization = res.body;
      },
      error: () => (this.loading = true),
    });
  }

  getAvailableAmigos(id: number): void {
    this.ligaService.getAmigos(id).subscribe({
      next: (res: HttpResponse<AmigoDetail[]>) => {
        this.amigos = res.body ?? [];
      },
      error: () => {
        this.loading = true;
      },
    });
  }

  previousState(): void {
    window.history.back();
  }

  addAmigoLiga(): void {
    this.resetErrors();
    const newFriend = this.getFriendFromForm();
    if (newFriend !== undefined && newFriend !== null) {
      this.ligaService.addAmigoLiga(newFriend, this.liga!.id!).subscribe({
        next: () => {
          this.addAmigoSuccess = true;
          this.getRankingGlobal(this.liga!.id!);
          this.getAvailableAmigos(this.liga!.id!);
        },
        error: response => this.processError(response),
      });
    } else {
      console.log('new friend input is empty');
    }
  }

  resetErrors(): void {
    this.amigoExists = false;
    this.addAmigoSuccess = false;
    this.error = false;
  }

  protected getFriendFromForm(): any {
    return this.friendForm.get(['newAmigo'])!.value;
  }

  private processError(response: HttpErrorResponse): void {
    console.log(response);
    if (response.status === 400 && response.error.errorKey === 'notfound') {
      this.errorUserExists = true;
    } else if (response.status === 400 && response.error.errorKey === 'amigoexists') {
      this.amigoExists = true;
    } else {
      this.error = true;
    }
  }
}
