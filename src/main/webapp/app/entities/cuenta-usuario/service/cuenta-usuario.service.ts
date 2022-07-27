import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICuentaUsuario, getCuentaUsuarioIdentifier } from '../cuenta-usuario.model';
import { IRanking } from '../ranking-model';

export type EntityResponseType = HttpResponse<ICuentaUsuario>;
export type EntityArrayResponseType = HttpResponse<ICuentaUsuario[]>;

export type EntityArrayResponseTypeRanking = HttpResponse<IRanking[]>;

@Injectable({ providedIn: 'root' })
export class CuentaUsuarioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cuenta-usuarios');
  protected rankingUrl = this.applicationConfigService.getEndpointFor('api/ranking');
  protected rankingUrlPersonal = this.applicationConfigService.getEndpointFor('api/ranking/personal');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cuentaUsuario: ICuentaUsuario): Observable<EntityResponseType> {
    return this.http.post<ICuentaUsuario>(this.resourceUrl, cuentaUsuario, { observe: 'response' });
  }

  update(cuentaUsuario: ICuentaUsuario): Observable<EntityResponseType> {
    return this.http.put<ICuentaUsuario>(`${this.resourceUrl}/${getCuentaUsuarioIdentifier(cuentaUsuario) as number}`, cuentaUsuario, {
      observe: 'response',
    });
  }

  partialUpdate(cuentaUsuario: ICuentaUsuario): Observable<EntityResponseType> {
    return this.http.patch<ICuentaUsuario>(`${this.resourceUrl}/${getCuentaUsuarioIdentifier(cuentaUsuario) as number}`, cuentaUsuario, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICuentaUsuario>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICuentaUsuario[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  rankingGlobal(req?: any): Observable<EntityArrayResponseTypeRanking> {
    const options = createRequestOption(req);
    return this.http.get<IRanking[]>(this.rankingUrl, { params: options, observe: 'response' });
  }

  rankingNacional(nacionalidad: string, req?: any): Observable<EntityArrayResponseTypeRanking> {
    const options = createRequestOption(req);
    return this.http.get<IRanking[]>(`${this.rankingUrl}/${nacionalidad}`, { params: options, observe: 'response' });
  }

  rankingPersonal(req?: any): Observable<EntityArrayResponseTypeRanking> {
    const options = createRequestOption(req);
    return this.http.get<IRanking[]>(this.rankingUrlPersonal, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCuentaUsuarioToCollectionIfMissing(
    cuentaUsuarioCollection: ICuentaUsuario[],
    ...cuentaUsuariosToCheck: (ICuentaUsuario | null | undefined)[]
  ): ICuentaUsuario[] {
    const cuentaUsuarios: ICuentaUsuario[] = cuentaUsuariosToCheck.filter(isPresent);
    if (cuentaUsuarios.length > 0) {
      const cuentaUsuarioCollectionIdentifiers = cuentaUsuarioCollection.map(
        cuentaUsuarioItem => getCuentaUsuarioIdentifier(cuentaUsuarioItem)!
      );
      const cuentaUsuariosToAdd = cuentaUsuarios.filter(cuentaUsuarioItem => {
        const cuentaUsuarioIdentifier = getCuentaUsuarioIdentifier(cuentaUsuarioItem);
        if (cuentaUsuarioIdentifier == null || cuentaUsuarioCollectionIdentifiers.includes(cuentaUsuarioIdentifier)) {
          return false;
        }
        cuentaUsuarioCollectionIdentifiers.push(cuentaUsuarioIdentifier);
        return true;
      });
      return [...cuentaUsuariosToAdd, ...cuentaUsuarioCollection];
    }
    return cuentaUsuarioCollection;
  }

  addRAnkingToCollectionIfMissing(
    cuentaUsuarioCollection: ICuentaUsuario[],
    ...cuentaUsuariosToCheck: (ICuentaUsuario | null | undefined)[]
  ): ICuentaUsuario[] {
    const cuentaUsuarios: ICuentaUsuario[] = cuentaUsuariosToCheck.filter(isPresent);
    if (cuentaUsuarios.length > 0) {
      const cuentaUsuarioCollectionIdentifiers = cuentaUsuarioCollection.map(
        cuentaUsuarioItem => getCuentaUsuarioIdentifier(cuentaUsuarioItem)!
      );
      const cuentaUsuariosToAdd = cuentaUsuarios.filter(cuentaUsuarioItem => {
        const cuentaUsuarioIdentifier = getCuentaUsuarioIdentifier(cuentaUsuarioItem);
        if (cuentaUsuarioIdentifier == null || cuentaUsuarioCollectionIdentifiers.includes(cuentaUsuarioIdentifier)) {
          return false;
        }
        cuentaUsuarioCollectionIdentifiers.push(cuentaUsuarioIdentifier);
        return true;
      });
      return [...cuentaUsuariosToAdd, ...cuentaUsuarioCollection];
    }
    return cuentaUsuarioCollection;
  }
}
