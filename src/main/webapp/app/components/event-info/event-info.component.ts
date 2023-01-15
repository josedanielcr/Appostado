import { Component, Input, OnInit } from '@angular/core';
import { IEvento } from '../../entities/evento/evento.model';

@Component({
  selector: 'jhi-event-info',
  templateUrl: './event-info.component.html',
  styleUrls: ['./event-info.component.scss'],
})
export class EventInfoComponent implements OnInit {
  @Input() event: IEvento | null;

  constructor() {
    return;
  }

  ngOnInit(): void {
    return;
  }
}
