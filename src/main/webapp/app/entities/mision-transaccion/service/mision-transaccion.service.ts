import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMisionTransaccion, getMisionTransaccionIdentifier } from '../mision-transaccion.model';

export type EntityResponseType = HttpResponse<IMisionTransaccion>;
export type EntityArrayResponseType = HttpResponse<IMisionTransaccion[]>;

@Injectable({ providedIn: 'root' })
export class MisionTransaccionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mision-transaccions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(misionTransaccion: IMisionTransaccion): Observable<EntityResponseType> {
    return this.http.post<IMisionTransaccion>(this.resourceUrl, misionTransaccion, { observe: 'response' });
  }

  update(misionTransaccion: IMisionTransaccion): Observable<EntityResponseType> {
    return this.http.put<IMisionTransaccion>(
      `${this.resourceUrl}/${getMisionTransaccionIdentifier(misionTransaccion) as number}`,
      misionTransaccion,
      { observe: 'response' }
    );
  }

  partialUpdate(misionTransaccion: IMisionTransaccion): Observable<EntityResponseType> {
    return this.http.patch<IMisionTransaccion>(
      `${this.resourceUrl}/${getMisionTransaccionIdentifier(misionTransaccion) as number}`,
      misionTransaccion,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMisionTransaccion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMisionTransaccion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMisionTransaccionToCollectionIfMissing(
    misionTransaccionCollection: IMisionTransaccion[],
    ...misionTransaccionsToCheck: (IMisionTransaccion | null | undefined)[]
  ): IMisionTransaccion[] {
    const misionTransaccions: IMisionTransaccion[] = misionTransaccionsToCheck.filter(isPresent);
    if (misionTransaccions.length > 0) {
      const misionTransaccionCollectionIdentifiers = misionTransaccionCollection.map(
        misionTransaccionItem => getMisionTransaccionIdentifier(misionTransaccionItem)!
      );
      const misionTransaccionsToAdd = misionTransaccions.filter(misionTransaccionItem => {
        const misionTransaccionIdentifier = getMisionTransaccionIdentifier(misionTransaccionItem);
        if (misionTransaccionIdentifier == null || misionTransaccionCollectionIdentifiers.includes(misionTransaccionIdentifier)) {
          return false;
        }
        misionTransaccionCollectionIdentifiers.push(misionTransaccionIdentifier);
        return true;
      });
      return [...misionTransaccionsToAdd, ...misionTransaccionCollection];
    }
    return misionTransaccionCollection;
  }
}
