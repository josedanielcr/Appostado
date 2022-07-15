import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILiga, getLigaIdentifier } from '../liga.model';

export type EntityResponseType = HttpResponse<ILiga>;
export type EntityArrayResponseType = HttpResponse<ILiga[]>;

@Injectable({ providedIn: 'root' })
export class LigaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ligas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(liga: ILiga): Observable<EntityResponseType> {
    return this.http.post<ILiga>(this.resourceUrl, liga, { observe: 'response' });
  }

  update(liga: ILiga): Observable<EntityResponseType> {
    return this.http.put<ILiga>(`${this.resourceUrl}/${getLigaIdentifier(liga) as number}`, liga, { observe: 'response' });
  }

  partialUpdate(liga: ILiga): Observable<EntityResponseType> {
    return this.http.patch<ILiga>(`${this.resourceUrl}/${getLigaIdentifier(liga) as number}`, liga, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILiga>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILiga[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLigaToCollectionIfMissing(ligaCollection: ILiga[], ...ligasToCheck: (ILiga | null | undefined)[]): ILiga[] {
    const ligas: ILiga[] = ligasToCheck.filter(isPresent);
    if (ligas.length > 0) {
      const ligaCollectionIdentifiers = ligaCollection.map(ligaItem => getLigaIdentifier(ligaItem)!);
      const ligasToAdd = ligas.filter(ligaItem => {
        const ligaIdentifier = getLigaIdentifier(ligaItem);
        if (ligaIdentifier == null || ligaCollectionIdentifiers.includes(ligaIdentifier)) {
          return false;
        }
        ligaCollectionIdentifiers.push(ligaIdentifier);
        return true;
      });
      return [...ligasToAdd, ...ligaCollection];
    }
    return ligaCollection;
  }
}
