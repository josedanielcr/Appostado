import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITransaccion, getTransaccionIdentifier } from '../transaccion.model';

export type EntityResponseType = HttpResponse<ITransaccion>;
export type EntityArrayResponseType = HttpResponse<ITransaccion[]>;

@Injectable({ providedIn: 'root' })
export class TransaccionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/transaccions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(transaccion: ITransaccion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transaccion);
    return this.http
      .post<ITransaccion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(transaccion: ITransaccion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transaccion);
    return this.http
      .put<ITransaccion>(`${this.resourceUrl}/${getTransaccionIdentifier(transaccion) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(transaccion: ITransaccion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transaccion);
    return this.http
      .patch<ITransaccion>(`${this.resourceUrl}/${getTransaccionIdentifier(transaccion) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITransaccion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITransaccion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTransaccionToCollectionIfMissing(
    transaccionCollection: ITransaccion[],
    ...transaccionsToCheck: (ITransaccion | null | undefined)[]
  ): ITransaccion[] {
    const transaccions: ITransaccion[] = transaccionsToCheck.filter(isPresent);
    if (transaccions.length > 0) {
      const transaccionCollectionIdentifiers = transaccionCollection.map(transaccionItem => getTransaccionIdentifier(transaccionItem)!);
      const transaccionsToAdd = transaccions.filter(transaccionItem => {
        const transaccionIdentifier = getTransaccionIdentifier(transaccionItem);
        if (transaccionIdentifier == null || transaccionCollectionIdentifiers.includes(transaccionIdentifier)) {
          return false;
        }
        transaccionCollectionIdentifiers.push(transaccionIdentifier);
        return true;
      });
      return [...transaccionsToAdd, ...transaccionCollection];
    }
    return transaccionCollection;
  }

  protected convertDateFromClient(transaccion: ITransaccion): ITransaccion {
    return Object.assign({}, transaccion, {
      fecha: transaccion.fecha?.isValid() ? transaccion.fecha.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fecha = res.body.fecha ? dayjs(res.body.fecha) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((transaccion: ITransaccion) => {
        transaccion.fecha = transaccion.fecha ? dayjs(transaccion.fecha) : undefined;
      });
    }
    return res;
  }
}
