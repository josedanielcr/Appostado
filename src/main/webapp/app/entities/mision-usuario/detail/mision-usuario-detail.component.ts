import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMisionUsuario } from '../mision-usuario.model';

@Component({
  selector: 'jhi-mision-usuario-detail',
  templateUrl: './mision-usuario-detail.component.html',
})
export class MisionUsuarioDetailComponent implements OnInit {
  misionUsuario: IMisionUsuario | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ misionUsuario }) => {
      this.misionUsuario = misionUsuario;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
