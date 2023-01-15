import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IParametro, getParametroIdentifier } from '../parametro.model';

export type EntityResponseType = HttpResponse<IParametro>;
export type EntityArrayResponseType = HttpResponse<IParametro[]>;

@Injectable({ providedIn: 'root' })
export class ParametroService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/parametros');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(parametro: IParametro): Observable<EntityResponseType> {
    return this.http.post<IParametro>(this.resourceUrl, parametro, { observe: 'response' });
  }

  update(parametro: IParametro): Observable<EntityResponseType> {
    return this.http.put<IParametro>(`${this.resourceUrl}/${getParametroIdentifier(parametro) as number}`, parametro, {
      observe: 'response',
    });
  }

  partialUpdate(parametro: IParametro): Observable<EntityResponseType> {
    return this.http.patch<IParametro>(`${this.resourceUrl}/${getParametroIdentifier(parametro) as number}`, parametro, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IParametro>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IParametro[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addParametroToCollectionIfMissing(
    parametroCollection: IParametro[],
    ...parametrosToCheck: (IParametro | null | undefined)[]
  ): IParametro[] {
    const parametros: IParametro[] = parametrosToCheck.filter(isPresent);
    if (parametros.length > 0) {
      const parametroCollectionIdentifiers = parametroCollection.map(parametroItem => getParametroIdentifier(parametroItem)!);
      const parametrosToAdd = parametros.filter(parametroItem => {
        const parametroIdentifier = getParametroIdentifier(parametroItem);
        if (parametroIdentifier == null || parametroCollectionIdentifiers.includes(parametroIdentifier)) {
          return false;
        }
        parametroCollectionIdentifiers.push(parametroIdentifier);
        return true;
      });
      return [...parametrosToAdd, ...parametroCollection];
    }
    return parametroCollection;
  }
}
