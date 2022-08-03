import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IEvento } from '../../entities/evento/evento.model';
import { EventoService } from '../../entities/evento/service/evento.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-bet-page',
  templateUrl: './bet-page.component.html',
  styleUrls: ['./bet-page.component.scss'],
})
export class BetPageComponent implements OnInit {
  public currentEvent: IEvento | null;
  private eventId: number;

  constructor(public activeRoute: ActivatedRoute, private eventService: EventoService) {
    return;
  }

  ngOnInit(): void {
    this.eventId = Number(this.activeRoute.snapshot.paramMap.get('id'));
    this.getCurrentEvent();
  }

  getCurrentEvent(): void {
    this.eventService.find(this.eventId).subscribe({
      next: (res: HttpResponse<IEvento>) => {
        this.currentEvent = res.body;
      },
    });
  }
}
