import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEvento } from '../evento.model';

@Component({
  selector: 'jhi-evento-detail',
  templateUrl: './evento-detail.component.html',
})
export class EventoDetailComponent implements OnInit {
  evento: IEvento | null = null;
  isPendiente = true;
  isEnProgreso = true;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evento }) => {
      this.evento = evento;
      if (this.evento?.estado === 'Pendiente') {
        this.isPendiente = false;
      } else if (this.evento?.estado === 'En progeso') {
        this.isEnProgreso = false;
      }
    });
  }

  previousState(): void {
    window.history.back();
  }
}
