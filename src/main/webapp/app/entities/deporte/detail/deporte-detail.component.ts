import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeporte } from '../deporte.model';

@Component({
  selector: 'jhi-deporte-detail',
  templateUrl: './deporte-detail.component.html',
})
export class DeporteDetailComponent implements OnInit {
  deporte: IDeporte | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deporte }) => {
      this.deporte = deporte;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
