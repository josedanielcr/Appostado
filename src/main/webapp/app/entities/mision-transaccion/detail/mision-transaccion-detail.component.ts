import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMisionTransaccion } from '../mision-transaccion.model';

@Component({
  selector: 'jhi-mision-transaccion-detail',
  templateUrl: './mision-transaccion-detail.component.html',
})
export class MisionTransaccionDetailComponent implements OnInit {
  misionTransaccion: IMisionTransaccion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ misionTransaccion }) => {
      this.misionTransaccion = misionTransaccion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
