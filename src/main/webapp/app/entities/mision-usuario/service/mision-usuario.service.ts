import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMisionUsuario, getMisionUsuarioIdentifier } from '../mision-usuario.model';

export type EntityResponseType = HttpResponse<IMisionUsuario>;
export type EntityArrayResponseType = HttpResponse<IMisionUsuario[]>;

@Injectable({ providedIn: 'root' })
export class MisionUsuarioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mision-usuarios');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(misionUsuario: IMisionUsuario): Observable<EntityResponseType> {
    return this.http.post<IMisionUsuario>(this.resourceUrl, misionUsuario, { observe: 'response' });
  }

  update(misionUsuario: IMisionUsuario): Observable<EntityResponseType> {
    return this.http.put<IMisionUsuario>(`${this.resourceUrl}/${getMisionUsuarioIdentifier(misionUsuario) as number}`, misionUsuario, {
      observe: 'response',
    });
  }

  partialUpdate(misionUsuario: IMisionUsuario): Observable<EntityResponseType> {
    return this.http.patch<IMisionUsuario>(`${this.resourceUrl}/${getMisionUsuarioIdentifier(misionUsuario) as number}`, misionUsuario, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMisionUsuario>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMisionUsuario[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMisionUsuarioToCollectionIfMissing(
    misionUsuarioCollection: IMisionUsuario[],
    ...misionUsuariosToCheck: (IMisionUsuario | null | undefined)[]
  ): IMisionUsuario[] {
    const misionUsuarios: IMisionUsuario[] = misionUsuariosToCheck.filter(isPresent);
    if (misionUsuarios.length > 0) {
      const misionUsuarioCollectionIdentifiers = misionUsuarioCollection.map(
        misionUsuarioItem => getMisionUsuarioIdentifier(misionUsuarioItem)!
      );
      const misionUsuariosToAdd = misionUsuarios.filter(misionUsuarioItem => {
        const misionUsuarioIdentifier = getMisionUsuarioIdentifier(misionUsuarioItem);
        if (misionUsuarioIdentifier == null || misionUsuarioCollectionIdentifiers.includes(misionUsuarioIdentifier)) {
          return false;
        }
        misionUsuarioCollectionIdentifiers.push(misionUsuarioIdentifier);
        return true;
      });
      return [...misionUsuariosToAdd, ...misionUsuarioCollection];
    }
    return misionUsuarioCollection;
  }
}
