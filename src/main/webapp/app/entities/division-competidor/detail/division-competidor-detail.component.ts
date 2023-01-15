import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDivisionCompetidor } from '../division-competidor.model';

@Component({
  selector: 'jhi-division-competidor-detail',
  templateUrl: './division-competidor-detail.component.html',
})
export class DivisionCompetidorDetailComponent implements OnInit {
  divisionCompetidor: IDivisionCompetidor | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ divisionCompetidor }) => {
      this.divisionCompetidor = divisionCompetidor;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
