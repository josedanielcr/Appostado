import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILigaUsuario } from '../liga-usuario.model';

@Component({
  selector: 'jhi-liga-usuario-detail',
  templateUrl: './liga-usuario-detail.component.html',
})
export class LigaUsuarioDetailComponent implements OnInit {
  ligaUsuario: ILigaUsuario | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ligaUsuario }) => {
      this.ligaUsuario = ligaUsuario;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
