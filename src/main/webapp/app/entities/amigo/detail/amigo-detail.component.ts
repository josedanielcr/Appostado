import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAmigo } from '../amigo.model';

@Component({
  selector: 'jhi-amigo-detail',
  templateUrl: './amigo-detail.component.html',
})
export class AmigoDetailComponent implements OnInit {
  amigo: IAmigo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ amigo }) => {
      this.amigo = amigo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
