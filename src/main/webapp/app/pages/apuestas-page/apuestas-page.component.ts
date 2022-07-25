import { Component, OnInit } from '@angular/core';
import { EventoService } from '../../entities/evento/service/evento.service';
import { HttpResponse } from '@angular/common/http';
import { IEvento } from '../../entities/evento/evento.model';
import { FormBuilder, FormGroup } from '@angular/forms';
import { DeporteService } from '../../entities/deporte/service/deporte.service';
import { DivisionService } from '../../entities/division/service/division.service';
import { IDeporte } from '../../entities/deporte/deporte.model';
import { IDivision } from '../../entities/division/division.model';

@Component({
  selector: 'jhi-apuestas-page',
  templateUrl: './apuestas-page.component.html',
  styleUrls: ['./apuestas-page.component.scss'],
})
export class ApuestasPageComponent implements OnInit {
  public eventSearchForm: FormGroup;
  public events: IEvento[] | null = [];
  public sports: IDeporte[] | null = [];
  public divisions: IDivision[] | null = [];
  public eventStates: string[] = [];
  public isEventLoadingErr = false;
  public searchError = '';
  public isLoading = false;

  constructor(
    private eventService: EventoService,
    private fb: FormBuilder,
    private sportService: DeporteService,
    private divisionService: DivisionService
  ) {}

  ngOnInit(): void {
    this.getSelectInputData();
    this.getAllEvents();
    this.createForm();
  }

  public submitForm(): void {
    if (this.eventSearchForm.invalid) {
      this.searchError = 'Error durante el proceso de busqueda, intente nuevamente';
      return;
    }

    const data = {
      sport: this.eventSearchForm.controls['sport'].value || -1,
      divsion: this.eventSearchForm.controls['division'].value || -1,
      eventStates: this.eventSearchForm.controls['eventStates'].value || 'empty',
    };
    this.searchError = '';
    this.searchEventByParams(data.sport, data.divsion, data.eventStates);
  }

  private getAllEvents(): void {
    this.isLoading = true;
    this.eventService.query().subscribe({
      next: (res: HttpResponse<IEvento[]>) => {
        this.setEventsArr(res.body);
        this.getEventStates(this.events);
        this.isLoading = false;
      },
      error: () => {
        this.isEventLoadingErr = true;
        this.isLoading = false;
      },
    });
  }

  private createForm(): void {
    this.eventSearchForm = this.fb.group({
      sport: [''],
      division: [''],
      eventStates: [''],
    });
  }

  private getSelectInputData(): void {
    this.sportService.query().subscribe({
      next: (res: HttpResponse<IDeporte[]>) => {
        this.sports = res.body;
      },
    });

    this.divisionService.query().subscribe({
      next: (res: HttpResponse<IDivision[]>) => {
        this.divisions = res.body;
      },
    });
  }

  private getEventStates(events: IEvento[] | null): void {
    events?.forEach(event => {
      if (event.estado != null) {
        this.eventStates.push(event.estado);
      }
    });
    this.eventStates = [...new Set(this.eventStates)];
  }

  private setEventsArr(events: IEvento[] | null): void {
    events?.forEach(event => {
      if (event.estado === 'En progreso') {
        this.events?.push(event);
      }
    });
  }

  private searchEventByParams(sport: number, division: number, state: string): void {
    this.isLoading = true;
    this.eventService.findBySportAndDivisionAndState(sport, division, state).subscribe({
      next: (res: HttpResponse<IEvento[]>) => {
        this.events = res.body;
        this.isLoading = false;
      },
    });
  }
}
