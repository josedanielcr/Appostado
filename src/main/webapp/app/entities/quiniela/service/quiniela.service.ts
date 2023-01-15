import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IQuiniela, getQuinielaIdentifier } from '../quiniela.model';

export type EntityResponseType = HttpResponse<IQuiniela>;
export type EntityArrayResponseType = HttpResponse<IQuiniela[]>;

@Injectable({ providedIn: 'root' })
export class QuinielaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/quinielas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(quiniela: IQuiniela): Observable<EntityResponseType> {
    return this.http.post<IQuiniela>(this.resourceUrl, quiniela, { observe: 'response' });
  }

  update(quiniela: IQuiniela): Observable<EntityResponseType> {
    return this.http.put<IQuiniela>(`${this.resourceUrl}/${getQuinielaIdentifier(quiniela) as number}`, quiniela, { observe: 'response' });
  }

  partialUpdate(quiniela: IQuiniela): Observable<EntityResponseType> {
    return this.http.patch<IQuiniela>(`${this.resourceUrl}/${getQuinielaIdentifier(quiniela) as number}`, quiniela, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IQuiniela>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IQuiniela[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addQuinielaToCollectionIfMissing(quinielaCollection: IQuiniela[], ...quinielasToCheck: (IQuiniela | null | undefined)[]): IQuiniela[] {
    const quinielas: IQuiniela[] = quinielasToCheck.filter(isPresent);
    if (quinielas.length > 0) {
      const quinielaCollectionIdentifiers = quinielaCollection.map(quinielaItem => getQuinielaIdentifier(quinielaItem)!);
      const quinielasToAdd = quinielas.filter(quinielaItem => {
        const quinielaIdentifier = getQuinielaIdentifier(quinielaItem);
        if (quinielaIdentifier == null || quinielaCollectionIdentifiers.includes(quinielaIdentifier)) {
          return false;
        }
        quinielaCollectionIdentifiers.push(quinielaIdentifier);
        return true;
      });
      return [...quinielasToAdd, ...quinielaCollection];
    }
    return quinielaCollection;
  }
}
