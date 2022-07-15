import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOpcionRol, getOpcionRolIdentifier } from '../opcion-rol.model';

export type EntityResponseType = HttpResponse<IOpcionRol>;
export type EntityArrayResponseType = HttpResponse<IOpcionRol[]>;

@Injectable({ providedIn: 'root' })
export class OpcionRolService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/opcion-rols');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(opcionRol: IOpcionRol): Observable<EntityResponseType> {
    return this.http.post<IOpcionRol>(this.resourceUrl, opcionRol, { observe: 'response' });
  }

  update(opcionRol: IOpcionRol): Observable<EntityResponseType> {
    return this.http.put<IOpcionRol>(`${this.resourceUrl}/${getOpcionRolIdentifier(opcionRol) as number}`, opcionRol, {
      observe: 'response',
    });
  }

  partialUpdate(opcionRol: IOpcionRol): Observable<EntityResponseType> {
    return this.http.patch<IOpcionRol>(`${this.resourceUrl}/${getOpcionRolIdentifier(opcionRol) as number}`, opcionRol, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOpcionRol>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOpcionRol[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOpcionRolToCollectionIfMissing(
    opcionRolCollection: IOpcionRol[],
    ...opcionRolsToCheck: (IOpcionRol | null | undefined)[]
  ): IOpcionRol[] {
    const opcionRols: IOpcionRol[] = opcionRolsToCheck.filter(isPresent);
    if (opcionRols.length > 0) {
      const opcionRolCollectionIdentifiers = opcionRolCollection.map(opcionRolItem => getOpcionRolIdentifier(opcionRolItem)!);
      const opcionRolsToAdd = opcionRols.filter(opcionRolItem => {
        const opcionRolIdentifier = getOpcionRolIdentifier(opcionRolItem);
        if (opcionRolIdentifier == null || opcionRolCollectionIdentifiers.includes(opcionRolIdentifier)) {
          return false;
        }
        opcionRolCollectionIdentifiers.push(opcionRolIdentifier);
        return true;
      });
      return [...opcionRolsToAdd, ...opcionRolCollection];
    }
    return opcionRolCollection;
  }
}
