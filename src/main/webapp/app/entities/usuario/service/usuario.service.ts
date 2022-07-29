import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUsuario, getUsuarioIdentifier } from '../usuario.model';

export type EntityResponseType = HttpResponse<IUsuario>;
export type EntityArrayResponseType = HttpResponse<IUsuario[]>;

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/usuarios');
  protected resourceUrlinac = this.applicationConfigService.getEndpointFor('api/usuarios/inactivar');
  protected resourceUrlActivar = this.applicationConfigService.getEndpointFor('api/usuarios/activar');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(usuario: IUsuario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(usuario);
    return this.http
      .post<IUsuario>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(usuario: IUsuario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(usuario);
    return this.http
      .put<IUsuario>(`${this.resourceUrl}/${getUsuarioIdentifier(usuario) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  getUsuarioLogueado(): Observable<EntityResponseType> {
    return this.http
      .get<IUsuario>('api/usuarios/logueado', { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  inactivar(id: number): void {
    console.log('A');
    this.http.delete(`${this.resourceUrlinac}/${id}`, { observe: 'response' });
  }

  activar(usuario: IUsuario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(usuario);
    return this.http
      .put<IUsuario>(`${this.resourceUrlActivar}/${getUsuarioIdentifier(usuario) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(usuario: IUsuario): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(usuario);
    return this.http
      .patch<IUsuario>(`${this.resourceUrl}/${getUsuarioIdentifier(usuario) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IUsuario>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUsuario[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUsuarioToCollectionIfMissing(usuarioCollection: IUsuario[], ...usuariosToCheck: (IUsuario | null | undefined)[]): IUsuario[] {
    const usuarios: IUsuario[] = usuariosToCheck.filter(isPresent);
    if (usuarios.length > 0) {
      const usuarioCollectionIdentifiers = usuarioCollection.map(usuarioItem => getUsuarioIdentifier(usuarioItem)!);
      const usuariosToAdd = usuarios.filter(usuarioItem => {
        const usuarioIdentifier = getUsuarioIdentifier(usuarioItem);
        if (usuarioIdentifier == null || usuarioCollectionIdentifiers.includes(usuarioIdentifier)) {
          return false;
        }
        usuarioCollectionIdentifiers.push(usuarioIdentifier);
        return true;
      });
      return [...usuariosToAdd, ...usuarioCollection];
    }
    return usuarioCollection;
  }

  protected convertDateFromClient(usuario: IUsuario): IUsuario {
    return Object.assign({}, usuario, {
      fechaNacimiento: usuario.fechaNacimiento?.isValid() ? usuario.fechaNacimiento.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaNacimiento = res.body.fechaNacimiento ? dayjs(res.body.fechaNacimiento) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((usuario: IUsuario) => {
        usuario.fechaNacimiento = usuario.fechaNacimiento ? dayjs(usuario.fechaNacimiento) : undefined;
      });
    }
    return res;
  }
}
