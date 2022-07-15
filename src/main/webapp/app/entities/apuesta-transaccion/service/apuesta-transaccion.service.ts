import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApuestaTransaccion, getApuestaTransaccionIdentifier } from '../apuesta-transaccion.model';

export type EntityResponseType = HttpResponse<IApuestaTransaccion>;
export type EntityArrayResponseType = HttpResponse<IApuestaTransaccion[]>;

@Injectable({ providedIn: 'root' })
export class ApuestaTransaccionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/apuesta-transaccions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(apuestaTransaccion: IApuestaTransaccion): Observable<EntityResponseType> {
    return this.http.post<IApuestaTransaccion>(this.resourceUrl, apuestaTransaccion, { observe: 'response' });
  }

  update(apuestaTransaccion: IApuestaTransaccion): Observable<EntityResponseType> {
    return this.http.put<IApuestaTransaccion>(
      `${this.resourceUrl}/${getApuestaTransaccionIdentifier(apuestaTransaccion) as number}`,
      apuestaTransaccion,
      { observe: 'response' }
    );
  }

  partialUpdate(apuestaTransaccion: IApuestaTransaccion): Observable<EntityResponseType> {
    return this.http.patch<IApuestaTransaccion>(
      `${this.resourceUrl}/${getApuestaTransaccionIdentifier(apuestaTransaccion) as number}`,
      apuestaTransaccion,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IApuestaTransaccion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IApuestaTransaccion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addApuestaTransaccionToCollectionIfMissing(
    apuestaTransaccionCollection: IApuestaTransaccion[],
    ...apuestaTransaccionsToCheck: (IApuestaTransaccion | null | undefined)[]
  ): IApuestaTransaccion[] {
    const apuestaTransaccions: IApuestaTransaccion[] = apuestaTransaccionsToCheck.filter(isPresent);
    if (apuestaTransaccions.length > 0) {
      const apuestaTransaccionCollectionIdentifiers = apuestaTransaccionCollection.map(
        apuestaTransaccionItem => getApuestaTransaccionIdentifier(apuestaTransaccionItem)!
      );
      const apuestaTransaccionsToAdd = apuestaTransaccions.filter(apuestaTransaccionItem => {
        const apuestaTransaccionIdentifier = getApuestaTransaccionIdentifier(apuestaTransaccionItem);
        if (apuestaTransaccionIdentifier == null || apuestaTransaccionCollectionIdentifiers.includes(apuestaTransaccionIdentifier)) {
          return false;
        }
        apuestaTransaccionCollectionIdentifiers.push(apuestaTransaccionIdentifier);
        return true;
      });
      return [...apuestaTransaccionsToAdd, ...apuestaTransaccionCollection];
    }
    return apuestaTransaccionCollection;
  }
}
