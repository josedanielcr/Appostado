import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQuiniela } from '../quiniela.model';

@Component({
  selector: 'jhi-quiniela-detail',
  templateUrl: './quiniela-detail.component.html',
})
export class QuinielaDetailComponent implements OnInit {
  quiniela: IQuiniela | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ quiniela }) => {
      this.quiniela = quiniela;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
