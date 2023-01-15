import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICanje } from '../canje.model';

@Component({
  selector: 'jhi-canje-detail',
  templateUrl: './canje-detail.component.html',
})
export class CanjeDetailComponent implements OnInit {
  canje: ICanje | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ canje }) => {
      this.canje = canje;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
