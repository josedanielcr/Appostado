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

  /**
   * Inicializa los eventos, estados y el formulario
   */
  ngOnInit(): void {
    this.getSelectInputData();
    this.getAllEvents();
    this.createForm();
  }

  /**
   * Una vez dado el submit al form, se validan los datos a buscar y se envian
   * a la funcion searchEventByParams que realiza la busqueda
   */
  public submitForm(): void {
    if (this.eventSearchForm.invalid) {
      this.searchError = 'Error durante el proceso de busqueda, intente nuevamente';
      return;
    }

    const data = {
      sport: this.eventSearchForm.controls['sport'].value || -1,
      division: this.eventSearchForm.controls['division'].value || -1,
      eventStates: this.eventSearchForm.controls['eventStates'].value || 'empty',
    };
    this.searchError = '';
    this.searchEventByParams(data.sport, data.division, data.eventStates);
  }

  /**
   * obtiene los eventos al iniciar la pantalla
   * @private
   */
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

  /**
   * Crea el formulario al iniciar la pantalla
   * @private
   */
  private createForm(): void {
    this.eventSearchForm = this.fb.group({
      sport: [''],
      division: [''],
      eventStates: [''],
    });
  }

  /**
   * obtiene los datos para llenar los selects de busqueda de eventos
   * @private
   */
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

  /**
   * obtiene los eventos en el inicio, seguidamente los ingresa en un arreglo
   * y estos son pasados por un Set para eliminar repetidos
   * @param events
   * @private
   */
  private getEventStates(events: IEvento[] | null): void {
    events?.forEach(event => {
      if (event.estado != null) {
        this.eventStates.push(event.estado);
      }
    });
    this.eventStates = [...new Set(this.eventStates)];
  }

  /**
   * Una vez obtenidos los eventos de una busqueda estos son ingresados en el array general de
   * eventos que es el que se muestra en pantalla
   * @param events
   * @private
   */
  private setEventsArr(events: IEvento[] | null): void {
    events?.forEach(event => {
      if (event.estado === 'Pendiente') {
        this.events?.push(event);
      }
    });
  }

  /**
   * Busca los eventos por los parametros que estan en la pantalla
   * @param sport
   * @param division
   * @param state
   * @private
   */
  private searchEventByParams(sport: number, division: number, state: string): void {
    this.isLoading = true;
    const searchButton: HTMLButtonElement | null = document.querySelector('#searchEventButton');
    searchButton!.disabled = true;
    this.eventService.findBySportAndDivisionAndState(sport, division, state).subscribe({
      next: (res: HttpResponse<IEvento[]>) => {
        this.events = [];
        this.setEventsArr(res.body);
        this.isLoading = false;
        searchButton!.disabled = false;
      },
    });
  }
}
