import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILiga, getLigaIdentifier } from '../liga.model';
import { IRanking } from '../../cuenta-usuario/ranking-model';
import { AmigoDetail } from '../../../pages/amigos-page/amigos-page.model';
import { ILigaUsuario } from '../../liga-usuario/liga-usuario.model';
import { map } from 'rxjs/operators';

export type EntityResponseType = HttpResponse<ILiga>;
export type EntityArrayResponseType = HttpResponse<ILiga[]>;

@Injectable({ providedIn: 'root' })
export class LigaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ligas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(liga: ILiga): Observable<EntityResponseType> {
    return this.http.post<ILiga>(this.resourceUrl, liga, { observe: 'response' });
  }

  update(liga: ILiga): Observable<EntityResponseType> {
    return this.http.put<ILiga>(`${this.resourceUrl}/${getLigaIdentifier(liga) as number}`, liga, { observe: 'response' });
  }

  partialUpdate(liga: ILiga): Observable<EntityResponseType> {
    return this.http.patch<ILiga>(`${this.resourceUrl}/${getLigaIdentifier(liga) as number}`, liga, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILiga>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILiga[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLigaToCollectionIfMissing(ligaCollection: ILiga[], ...ligasToCheck: (ILiga | null | undefined)[]): ILiga[] {
    const ligas: ILiga[] = ligasToCheck.filter(isPresent);
    if (ligas.length > 0) {
      const ligaCollectionIdentifiers = ligaCollection.map(ligaItem => getLigaIdentifier(ligaItem)!);
      const ligasToAdd = ligas.filter(ligaItem => {
        const ligaIdentifier = getLigaIdentifier(ligaItem);
        if (ligaIdentifier == null || ligaCollectionIdentifiers.includes(ligaIdentifier)) {
          return false;
        }
        ligaCollectionIdentifiers.push(ligaIdentifier);
        return true;
      });
      return [...ligasToAdd, ...ligaCollection];
    }
    return ligaCollection;
  }

  getMisLigas(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILiga[]>(`${this.resourceUrl}/listar/misligas`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDescripcionArrayFromServer(res)));
  }

  getLigasAmigos(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILiga[]>(`${this.resourceUrl}/listar/ligasamigos`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDescripcionArrayFromServer(res)));
  }

  getEditAuthorization(id: number): Observable<HttpResponse<boolean>> {
    return this.http.get<boolean>(`${this.resourceUrl}/owner/${id}`, { observe: 'response' });
  }

  getRanking(id: number): Observable<HttpResponse<IRanking[]>> {
    const options = createRequestOption(id);
    return this.http.get<IRanking[]>(`${this.resourceUrl}/ranking/${id}`, { params: options, observe: 'response' });
  }

  getAmigos(id: number): Observable<HttpResponse<AmigoDetail[]>> {
    const options = createRequestOption(id);
    return this.http.get<AmigoDetail[]>(`${this.resourceUrl}/listaramigos/${id}`, { params: options, observe: 'response' });
  }

  addAmigoLiga(amigo: string, id: number): Observable<HttpResponse<ILigaUsuario>> {
    return this.http.post<ILigaUsuario>(`${this.resourceUrl}/agregaramigo/${id}`, amigo, { observe: 'response' });
  }

  convertDescripcionFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.descripcion = res.body.descripcion ? this.splitDescripcion(res.body.descripcion) : undefined;
    }
    return res;
  }
  splitDescripcion(desc: string): string {
    const processed = desc.split('//', 2);
    console.log(processed);
    return processed[1];
  }

  abandonarLiga(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/abandonar/${id}`, { observe: 'response' });
  }

  protected convertDescripcionArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((liga: ILiga) => {
        liga.descripcion = liga.descripcion ? this.splitDescripcion(liga.descripcion) : undefined;
      });
    }
    return res;
  }
}
