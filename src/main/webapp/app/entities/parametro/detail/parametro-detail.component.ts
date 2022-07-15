import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IParametro } from '../parametro.model';

@Component({
  selector: 'jhi-parametro-detail',
  templateUrl: './parametro-detail.component.html',
})
export class ParametroDetailComponent implements OnInit {
  parametro: IParametro | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ parametro }) => {
      this.parametro = parametro;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
