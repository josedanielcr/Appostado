import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IApuesta } from '../apuesta.model';

@Component({
  selector: 'jhi-apuesta-detail',
  templateUrl: './apuesta-detail.component.html',
})
export class ApuestaDetailComponent implements OnInit {
  apuesta: IApuesta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ apuesta }) => {
      this.apuesta = apuesta;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
