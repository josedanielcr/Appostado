import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEvento, getEventoIdentifier } from '../evento.model';
import { ICompetidor } from '../../competidor/competidor.model';

export type EntityResponseType = HttpResponse<IEvento>;
export type EntityArrayResponseType = HttpResponse<IEvento[]>;

@Injectable({ providedIn: 'root' })
export class EventoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/eventos');
  protected resourceUrlCancel = this.applicationConfigService.getEndpointFor('api/eventos/cancelar');
  protected resourceUrlResolver = this.applicationConfigService.getEndpointFor('api/eventos/resolver');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(evento: IEvento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evento);
    return this.http
      .post<IEvento>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(evento: IEvento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evento);
    return this.http
      .put<IEvento>(`${this.resourceUrl}/${getEventoIdentifier(evento) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  resolver(evento: IEvento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evento);
    return this.http
      .put<IEvento>(`${this.resourceUrlResolver}/${getEventoIdentifier(evento) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(evento: IEvento): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evento);
    return this.http
      .patch<IEvento>(`${this.resourceUrl}/${getEventoIdentifier(evento) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEvento>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEvento[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  cancelar(id: number): Observable<EntityResponseType> {
    return this.http.put(`${this.resourceUrlCancel}/${id}`, id, { observe: 'response' });
  }

  addEventoToCollectionIfMissing(eventoCollection: IEvento[], ...eventosToCheck: (IEvento | null | undefined)[]): IEvento[] {
    const eventos: IEvento[] = eventosToCheck.filter(isPresent);
    if (eventos.length > 0) {
      const eventoCollectionIdentifiers = eventoCollection.map(eventoItem => getEventoIdentifier(eventoItem)!);
      const eventosToAdd = eventos.filter(eventoItem => {
        const eventoIdentifier = getEventoIdentifier(eventoItem);
        if (eventoIdentifier == null || eventoCollectionIdentifiers.includes(eventoIdentifier)) {
          return false;
        }
        eventoCollectionIdentifiers.push(eventoIdentifier);
        return true;
      });
      return [...eventosToAdd, ...eventoCollection];
    }
    return eventoCollection;
  }

  findBySportAndDivisionAndState(pSport: number, pDivision: number, pState: string): Observable<EntityArrayResponseType> {
    return this.http
      .get<IEvento[]>(`${this.resourceUrl}/${pSport}/${pDivision}/${pState}`, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(evento: IEvento): IEvento {
    return Object.assign({}, evento, {
      fecha: evento.fecha?.isValid() ? evento.fecha.format(DATE_FORMAT) : undefined,
      horaInicio: evento.horaInicio?.isValid() ? evento.horaInicio.toJSON() : undefined,
      horaFin: evento.horaFin?.isValid() ? evento.horaFin.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fecha = res.body.fecha ? dayjs(res.body.fecha) : undefined;
      res.body.horaInicio = res.body.horaInicio ? dayjs(res.body.horaInicio) : undefined;
      res.body.horaFin = res.body.horaFin ? dayjs(res.body.horaFin) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((evento: IEvento) => {
        evento.fecha = evento.fecha ? dayjs(evento.fecha) : undefined;
        evento.horaInicio = evento.horaInicio ? dayjs(evento.horaInicio) : undefined;
        evento.horaFin = evento.horaFin ? dayjs(evento.horaFin) : undefined;
      });
    }
    return res;
  }
}
