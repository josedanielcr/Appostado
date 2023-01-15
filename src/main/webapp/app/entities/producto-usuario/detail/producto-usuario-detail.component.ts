import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProductoUsuario } from '../producto-usuario.model';

@Component({
  selector: 'jhi-producto-usuario-detail',
  templateUrl: './producto-usuario-detail.component.html',
})
export class ProductoUsuarioDetailComponent implements OnInit {
  productoUsuario: IProductoUsuario | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productoUsuario }) => {
      this.productoUsuario = productoUsuario;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
