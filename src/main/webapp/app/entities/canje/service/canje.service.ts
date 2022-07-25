import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICanje, getCanjeIdentifier } from '../canje.model';

export type EntityResponseType = HttpResponse<ICanje>;
export type EntityArrayResponseType = HttpResponse<ICanje[]>;

@Injectable({ providedIn: 'root' })
export class CanjeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/canjes');
  protected resourceUrlProceso = this.applicationConfigService.getEndpointFor('api/canjes/validar');
  protected resourceUrlPendientes = this.applicationConfigService.getEndpointFor('api/canjes/pendientes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(canje: ICanje): Observable<EntityResponseType> {
    return this.http.post<ICanje>(this.resourceUrl, canje, { observe: 'response' });
  }

  update(canje: ICanje): Observable<EntityResponseType> {
    return this.http.put<ICanje>(`${this.resourceUrl}/${getCanjeIdentifier(canje) as number}`, canje, { observe: 'response' });
  }

  partialUpdate(canje: ICanje): Observable<EntityResponseType> {
    return this.http.patch<ICanje>(`${this.resourceUrl}/${getCanjeIdentifier(canje) as number}`, canje, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICanje>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICanje[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  getPendientes(): Observable<EntityArrayResponseType> {
    return this.http.get<ICanje[]>(this.resourceUrlPendientes, { observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  /*realizarCanje(idPremio: number): Observable<EntityResponseType> {
      return this.http.get(`${this.resourceUrlProceso}/${idPremio}`, { observe: 'response' });
  }*/

  addCanjeToCollectionIfMissing(canjeCollection: ICanje[], ...canjesToCheck: (ICanje | null | undefined)[]): ICanje[] {
    const canjes: ICanje[] = canjesToCheck.filter(isPresent);
    if (canjes.length > 0) {
      const canjeCollectionIdentifiers = canjeCollection.map(canjeItem => getCanjeIdentifier(canjeItem)!);
      const canjesToAdd = canjes.filter(canjeItem => {
        const canjeIdentifier = getCanjeIdentifier(canjeItem);
        if (canjeIdentifier == null || canjeCollectionIdentifiers.includes(canjeIdentifier)) {
          return false;
        }
        canjeCollectionIdentifiers.push(canjeIdentifier);
        return true;
      });
      return [...canjesToAdd, ...canjeCollection];
    }
    return canjeCollection;
  }
}
