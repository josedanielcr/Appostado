import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompetidor, getCompetidorIdentifier } from '../competidor.model';

export type EntityResponseType = HttpResponse<ICompetidor>;
export type EntityArrayResponseType = HttpResponse<ICompetidor[]>;

@Injectable({ providedIn: 'root' })
export class CompetidorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/competidors');
  protected resourceUrlComp = this.applicationConfigService.getEndpointFor('api/eventos/competidores');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(competidor: ICompetidor): Observable<EntityResponseType> {
    return this.http.post<ICompetidor>(this.resourceUrl, competidor, { observe: 'response' });
  }

  update(competidor: ICompetidor): Observable<EntityResponseType> {
    return this.http.put<ICompetidor>(`${this.resourceUrl}/${getCompetidorIdentifier(competidor) as number}`, competidor, {
      observe: 'response',
    });
  }

  partialUpdate(competidor: ICompetidor): Observable<EntityResponseType> {
    return this.http.patch<ICompetidor>(`${this.resourceUrl}/${getCompetidorIdentifier(competidor) as number}`, competidor, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICompetidor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICompetidor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  getCompetidoresEvento(id: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICompetidor[]>(`${this.resourceUrlComp}/${id}`, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCompetidorToCollectionIfMissing(
    competidorCollection: ICompetidor[],
    ...competidorsToCheck: (ICompetidor | null | undefined)[]
  ): ICompetidor[] {
    const competidors: ICompetidor[] = competidorsToCheck.filter(isPresent);
    if (competidors.length > 0) {
      const competidorCollectionIdentifiers = competidorCollection.map(competidorItem => getCompetidorIdentifier(competidorItem)!);
      const competidorsToAdd = competidors.filter(competidorItem => {
        const competidorIdentifier = getCompetidorIdentifier(competidorItem);
        if (competidorIdentifier == null || competidorCollectionIdentifiers.includes(competidorIdentifier)) {
          return false;
        }
        competidorCollectionIdentifiers.push(competidorIdentifier);
        return true;
      });
      return [...competidorsToAdd, ...competidorCollection];
    }
    return competidorCollection;
  }
}
