import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMision } from '../mision.model';

@Component({
  selector: 'jhi-mision-detail',
  templateUrl: './mision-detail.component.html',
})
export class MisionDetailComponent implements OnInit {
  mision: IMision | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mision }) => {
      this.mision = mision;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
