import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILiga } from '../liga.model';

@Component({
  selector: 'jhi-liga-detail',
  templateUrl: './liga-detail.component.html',
})
export class LigaDetailComponent implements OnInit {
  liga: ILiga | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ liga }) => {
      this.liga = liga;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
