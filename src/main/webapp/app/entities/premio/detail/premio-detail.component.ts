import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPremio } from '../premio.model';

@Component({
  selector: 'jhi-premio-detail',
  templateUrl: './premio-detail.component.html',
})
export class PremioDetailComponent implements OnInit {
  premio: IPremio | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ premio }) => {
      this.premio = premio;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
