import { Component, Input, OnInit } from '@angular/core';
import { IEvento } from '../../entities/evento/evento.model';
import { Router } from '@angular/router';
import { ApuestaService } from '../../entities/apuesta/service/apuesta.service';
import { IApuesta } from '../../entities/apuesta/apuesta.model';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-event-card',
  templateUrl: './event-card.component.html',
  styleUrls: ['./event-card.component.scss'],
})
export class EventCardComponent implements OnInit {
  @Input() event: IEvento;
  public currentRoute: string;
  public apuestaActiva: IApuesta | null;

  constructor(private router: Router, private apuestaService: ApuestaService) {
    this.currentRoute = this.router.url;
  }

  ngOnInit(): void {
    if (this.currentRoute !== '/panel') {
      return;
    }
    this.apuestaService.getByEventIdAndUser(this.event.id).subscribe({
      next: (res: HttpResponse<IApuesta>) => {
        this.apuestaActiva = res.body;
      },
    });
  }
}
