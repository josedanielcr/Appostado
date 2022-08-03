import { Component, Input, OnInit } from '@angular/core';
import { ApuestaService } from '../../entities/apuesta/service/apuesta.service';
import { HttpResponse } from '@angular/common/http';
import { IApuesta } from '../../entities/apuesta/apuesta.model';
import { IEvento } from '../../entities/evento/evento.model';

@Component({
  selector: 'jhi-event-participants',
  templateUrl: './event-participants.component.html',
  styleUrls: ['./event-participants.component.scss'],
})
export class EventParticipantsComponent implements OnInit {
  @Input() event: IEvento | null;
  public eventParticipantsCounter: number | undefined;

  constructor(private apuestasService: ApuestaService) {
    return;
  }

  ngOnInit(): void {
    const eventId = this.event!.id!.toString();
    this.getNumberOfParticipants(eventId);
  }

  getNumberOfParticipants(eventId: string | null): void {
    this.apuestasService.getApuestasByEventId(eventId).subscribe({
      next: (res: HttpResponse<IApuesta[]>) => {
        this.eventParticipantsCounter = res.body?.length;
      },
    });
  }
}
