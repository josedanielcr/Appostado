import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductoUsuario, getProductoUsuarioIdentifier } from '../producto-usuario.model';

export type EntityResponseType = HttpResponse<IProductoUsuario>;
export type EntityArrayResponseType = HttpResponse<IProductoUsuario[]>;

@Injectable({ providedIn: 'root' })
export class ProductoUsuarioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/producto-usuarios');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(productoUsuario: IProductoUsuario): Observable<EntityResponseType> {
    return this.http.post<IProductoUsuario>(this.resourceUrl, productoUsuario, { observe: 'response' });
  }

  update(productoUsuario: IProductoUsuario): Observable<EntityResponseType> {
    return this.http.put<IProductoUsuario>(
      `${this.resourceUrl}/${getProductoUsuarioIdentifier(productoUsuario) as number}`,
      productoUsuario,
      { observe: 'response' }
    );
  }

  partialUpdate(productoUsuario: IProductoUsuario): Observable<EntityResponseType> {
    return this.http.patch<IProductoUsuario>(
      `${this.resourceUrl}/${getProductoUsuarioIdentifier(productoUsuario) as number}`,
      productoUsuario,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProductoUsuario>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProductoUsuario[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProductoUsuarioToCollectionIfMissing(
    productoUsuarioCollection: IProductoUsuario[],
    ...productoUsuariosToCheck: (IProductoUsuario | null | undefined)[]
  ): IProductoUsuario[] {
    const productoUsuarios: IProductoUsuario[] = productoUsuariosToCheck.filter(isPresent);
    if (productoUsuarios.length > 0) {
      const productoUsuarioCollectionIdentifiers = productoUsuarioCollection.map(
        productoUsuarioItem => getProductoUsuarioIdentifier(productoUsuarioItem)!
      );
      const productoUsuariosToAdd = productoUsuarios.filter(productoUsuarioItem => {
        const productoUsuarioIdentifier = getProductoUsuarioIdentifier(productoUsuarioItem);
        if (productoUsuarioIdentifier == null || productoUsuarioCollectionIdentifiers.includes(productoUsuarioIdentifier)) {
          return false;
        }
        productoUsuarioCollectionIdentifiers.push(productoUsuarioIdentifier);
        return true;
      });
      return [...productoUsuariosToAdd, ...productoUsuarioCollection];
    }
    return productoUsuarioCollection;
  }
}
