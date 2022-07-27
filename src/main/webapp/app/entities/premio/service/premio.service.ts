import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPremio, getPremioIdentifier } from '../premio.model';

export type EntityResponseType = HttpResponse<IPremio>;
export type EntityArrayResponseType = HttpResponse<IPremio[]>;

@Injectable({ providedIn: 'root' })
export class PremioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/premios');
  protected resourceUrlPremiosActivos = this.applicationConfigService.getEndpointFor('api/premios/activos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(premio: IPremio): Observable<EntityResponseType> {
    return this.http.post<IPremio>(this.resourceUrl, premio, { observe: 'response' });
  }

  update(premio: IPremio): Observable<EntityResponseType> {
    return this.http.put<IPremio>(`${this.resourceUrl}/${getPremioIdentifier(premio) as number}`, premio, { observe: 'response' });
  }

  partialUpdate(premio: IPremio): Observable<EntityResponseType> {
    return this.http.patch<IPremio>(`${this.resourceUrl}/${getPremioIdentifier(premio) as number}`, premio, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPremio>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
  /**
  encuentra los premios en estado activo
  */
  findActivos(): Observable<EntityArrayResponseType> {
    return this.http.get<IPremio[]>(this.resourceUrlPremiosActivos, { observe: 'response' });
  }

  /**
    encuentra los premios en estado activo y los ordena dependiendo del string que recibe
    */

  findActivosFiltro(acomodo: number): Observable<EntityArrayResponseType> {
    return this.http.get<IPremio[]>(`${this.resourceUrlPremiosActivos}/${acomodo}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPremio[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPremioToCollectionIfMissing(premioCollection: IPremio[], ...premiosToCheck: (IPremio | null | undefined)[]): IPremio[] {
    const premios: IPremio[] = premiosToCheck.filter(isPresent);
    if (premios.length > 0) {
      const premioCollectionIdentifiers = premioCollection.map(premioItem => getPremioIdentifier(premioItem)!);
      const premiosToAdd = premios.filter(premioItem => {
        const premioIdentifier = getPremioIdentifier(premioItem);
        if (premioIdentifier == null || premioCollectionIdentifiers.includes(premioIdentifier)) {
          return false;
        }
        premioCollectionIdentifiers.push(premioIdentifier);
        return true;
      });
      return [...premiosToAdd, ...premioCollection];
    }
    return premioCollection;
  }
}
