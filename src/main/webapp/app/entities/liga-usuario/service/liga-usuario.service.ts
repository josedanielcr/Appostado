import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILigaUsuario, getLigaUsuarioIdentifier } from '../liga-usuario.model';

export type EntityResponseType = HttpResponse<ILigaUsuario>;
export type EntityArrayResponseType = HttpResponse<ILigaUsuario[]>;

@Injectable({ providedIn: 'root' })
export class LigaUsuarioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/liga-usuarios');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ligaUsuario: ILigaUsuario): Observable<EntityResponseType> {
    return this.http.post<ILigaUsuario>(this.resourceUrl, ligaUsuario, { observe: 'response' });
  }

  update(ligaUsuario: ILigaUsuario): Observable<EntityResponseType> {
    return this.http.put<ILigaUsuario>(`${this.resourceUrl}/${getLigaUsuarioIdentifier(ligaUsuario) as number}`, ligaUsuario, {
      observe: 'response',
    });
  }

  partialUpdate(ligaUsuario: ILigaUsuario): Observable<EntityResponseType> {
    return this.http.patch<ILigaUsuario>(`${this.resourceUrl}/${getLigaUsuarioIdentifier(ligaUsuario) as number}`, ligaUsuario, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILigaUsuario>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILigaUsuario[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLigaUsuarioToCollectionIfMissing(
    ligaUsuarioCollection: ILigaUsuario[],
    ...ligaUsuariosToCheck: (ILigaUsuario | null | undefined)[]
  ): ILigaUsuario[] {
    const ligaUsuarios: ILigaUsuario[] = ligaUsuariosToCheck.filter(isPresent);
    if (ligaUsuarios.length > 0) {
      const ligaUsuarioCollectionIdentifiers = ligaUsuarioCollection.map(ligaUsuarioItem => getLigaUsuarioIdentifier(ligaUsuarioItem)!);
      const ligaUsuariosToAdd = ligaUsuarios.filter(ligaUsuarioItem => {
        const ligaUsuarioIdentifier = getLigaUsuarioIdentifier(ligaUsuarioItem);
        if (ligaUsuarioIdentifier == null || ligaUsuarioCollectionIdentifiers.includes(ligaUsuarioIdentifier)) {
          return false;
        }
        ligaUsuarioCollectionIdentifiers.push(ligaUsuarioIdentifier);
        return true;
      });
      return [...ligaUsuariosToAdd, ...ligaUsuarioCollection];
    }
    return ligaUsuarioCollection;
  }
}
