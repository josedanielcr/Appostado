import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDivisionCompetidor, getDivisionCompetidorIdentifier } from '../division-competidor.model';

export type EntityResponseType = HttpResponse<IDivisionCompetidor>;
export type EntityArrayResponseType = HttpResponse<IDivisionCompetidor[]>;

@Injectable({ providedIn: 'root' })
export class DivisionCompetidorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/division-competidors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(divisionCompetidor: IDivisionCompetidor): Observable<EntityResponseType> {
    return this.http.post<IDivisionCompetidor>(this.resourceUrl, divisionCompetidor, { observe: 'response' });
  }

  update(divisionCompetidor: IDivisionCompetidor): Observable<EntityResponseType> {
    return this.http.put<IDivisionCompetidor>(
      `${this.resourceUrl}/${getDivisionCompetidorIdentifier(divisionCompetidor) as number}`,
      divisionCompetidor,
      { observe: 'response' }
    );
  }

  partialUpdate(divisionCompetidor: IDivisionCompetidor): Observable<EntityResponseType> {
    return this.http.patch<IDivisionCompetidor>(
      `${this.resourceUrl}/${getDivisionCompetidorIdentifier(divisionCompetidor) as number}`,
      divisionCompetidor,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDivisionCompetidor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDivisionCompetidor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDivisionCompetidorToCollectionIfMissing(
    divisionCompetidorCollection: IDivisionCompetidor[],
    ...divisionCompetidorsToCheck: (IDivisionCompetidor | null | undefined)[]
  ): IDivisionCompetidor[] {
    const divisionCompetidors: IDivisionCompetidor[] = divisionCompetidorsToCheck.filter(isPresent);
    if (divisionCompetidors.length > 0) {
      const divisionCompetidorCollectionIdentifiers = divisionCompetidorCollection.map(
        divisionCompetidorItem => getDivisionCompetidorIdentifier(divisionCompetidorItem)!
      );
      const divisionCompetidorsToAdd = divisionCompetidors.filter(divisionCompetidorItem => {
        const divisionCompetidorIdentifier = getDivisionCompetidorIdentifier(divisionCompetidorItem);
        if (divisionCompetidorIdentifier == null || divisionCompetidorCollectionIdentifiers.includes(divisionCompetidorIdentifier)) {
          return false;
        }
        divisionCompetidorCollectionIdentifiers.push(divisionCompetidorIdentifier);
        return true;
      });
      return [...divisionCompetidorsToAdd, ...divisionCompetidorCollection];
    }
    return divisionCompetidorCollection;
  }
}
