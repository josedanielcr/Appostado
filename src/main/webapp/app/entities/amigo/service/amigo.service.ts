import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAmigo, getAmigoIdentifier } from '../amigo.model';

export type EntityResponseType = HttpResponse<IAmigo>;
export type EntityArrayResponseType = HttpResponse<IAmigo[]>;

@Injectable({ providedIn: 'root' })
export class AmigoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/amigos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(amigo: IAmigo): Observable<EntityResponseType> {
    return this.http.post<IAmigo>(this.resourceUrl, amigo, { observe: 'response' });
  }

  update(amigo: IAmigo): Observable<EntityResponseType> {
    return this.http.put<IAmigo>(`${this.resourceUrl}/${getAmigoIdentifier(amigo) as number}`, amigo, { observe: 'response' });
  }

  partialUpdate(amigo: IAmigo): Observable<EntityResponseType> {
    return this.http.patch<IAmigo>(`${this.resourceUrl}/${getAmigoIdentifier(amigo) as number}`, amigo, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAmigo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAmigo[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAmigoToCollectionIfMissing(amigoCollection: IAmigo[], ...amigosToCheck: (IAmigo | null | undefined)[]): IAmigo[] {
    const amigos: IAmigo[] = amigosToCheck.filter(isPresent);
    if (amigos.length > 0) {
      const amigoCollectionIdentifiers = amigoCollection.map(amigoItem => getAmigoIdentifier(amigoItem)!);
      const amigosToAdd = amigos.filter(amigoItem => {
        const amigoIdentifier = getAmigoIdentifier(amigoItem);
        if (amigoIdentifier == null || amigoCollectionIdentifiers.includes(amigoIdentifier)) {
          return false;
        }
        amigoCollectionIdentifiers.push(amigoIdentifier);
        return true;
      });
      return [...amigosToAdd, ...amigoCollection];
    }
    return amigoCollection;
  }
}
