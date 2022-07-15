import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IApuestaTransaccion } from '../apuesta-transaccion.model';

@Component({
  selector: 'jhi-apuesta-transaccion-detail',
  templateUrl: './apuesta-transaccion-detail.component.html',
})
export class ApuestaTransaccionDetailComponent implements OnInit {
  apuestaTransaccion: IApuestaTransaccion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ apuestaTransaccion }) => {
      this.apuestaTransaccion = apuestaTransaccion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
