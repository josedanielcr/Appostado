import { Component, Input, OnInit } from '@angular/core';
import { IEvento } from '../../entities/evento/evento.model';

@Component({
  selector: 'jhi-event-card',
  templateUrl: './event-card.component.html',
  styleUrls: ['./event-card.component.scss'],
})
export class EventCardComponent implements OnInit {
  @Input() event: IEvento;

  constructor() {
    return;
  }

  ngOnInit(): void {
    return;
  }
}
