import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDeporte, getDeporteIdentifier } from '../deporte.model';

export type EntityResponseType = HttpResponse<IDeporte>;
export type EntityArrayResponseType = HttpResponse<IDeporte[]>;

@Injectable({ providedIn: 'root' })
export class DeporteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/deportes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(deporte: IDeporte): Observable<EntityResponseType> {
    return this.http.post<IDeporte>(this.resourceUrl, deporte, { observe: 'response' });
  }

  update(deporte: IDeporte): Observable<EntityResponseType> {
    return this.http.put<IDeporte>(`${this.resourceUrl}/${getDeporteIdentifier(deporte) as number}`, deporte, { observe: 'response' });
  }

  partialUpdate(deporte: IDeporte): Observable<EntityResponseType> {
    return this.http.patch<IDeporte>(`${this.resourceUrl}/${getDeporteIdentifier(deporte) as number}`, deporte, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDeporte>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeporte[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDeporteToCollectionIfMissing(deporteCollection: IDeporte[], ...deportesToCheck: (IDeporte | null | undefined)[]): IDeporte[] {
    const deportes: IDeporte[] = deportesToCheck.filter(isPresent);
    if (deportes.length > 0) {
      const deporteCollectionIdentifiers = deporteCollection.map(deporteItem => getDeporteIdentifier(deporteItem)!);
      const deportesToAdd = deportes.filter(deporteItem => {
        const deporteIdentifier = getDeporteIdentifier(deporteItem);
        if (deporteIdentifier == null || deporteCollectionIdentifiers.includes(deporteIdentifier)) {
          return false;
        }
        deporteCollectionIdentifiers.push(deporteIdentifier);
        return true;
      });
      return [...deportesToAdd, ...deporteCollection];
    }
    return deporteCollection;
  }
}
