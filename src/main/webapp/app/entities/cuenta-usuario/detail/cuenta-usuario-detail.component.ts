import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICuentaUsuario } from '../cuenta-usuario.model';

@Component({
  selector: 'jhi-cuenta-usuario-detail',
  templateUrl: './cuenta-usuario-detail.component.html',
})
export class CuentaUsuarioDetailComponent implements OnInit {
  cuentaUsuario: ICuentaUsuario | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cuentaUsuario }) => {
      this.cuentaUsuario = cuentaUsuario;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
