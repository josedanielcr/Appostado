import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOpcionRol } from '../opcion-rol.model';

@Component({
  selector: 'jhi-opcion-rol-detail',
  templateUrl: './opcion-rol-detail.component.html',
})
export class OpcionRolDetailComponent implements OnInit {
  opcionRol: IOpcionRol | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ opcionRol }) => {
      this.opcionRol = opcionRol;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
