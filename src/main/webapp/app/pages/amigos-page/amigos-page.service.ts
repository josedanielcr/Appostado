import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAmigo, getAmigoIdentifier } from '../../entities/amigo/amigo.model';
import { AmigoDetail, NotificacionAmigo } from './amigos-page.model';
import { INotificacion } from '../../entities/notificacion/notificacion.model';

export type EntityResponseType = HttpResponse<IAmigo>;
export type EntityArrayResponseType = HttpResponse<IAmigo[]>;

@Injectable({ providedIn: 'root' })
export class AmigosPageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/amigos');
  protected resourceUrlNotificacion = this.applicationConfigService.getEndpointFor('api/notificacions/amistad');

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

  getNotificacionesAmigos(req?: any): Observable<HttpResponse<NotificacionAmigo[]>> {
    const options = createRequestOption(req);
    return this.http.get<NotificacionAmigo[]>(this.applicationConfigService.getEndpointFor('api/notificacions/amistades'), {
      params: options,
      observe: 'response',
    });
  }

  getAmigos(req?: any): Observable<HttpResponse<AmigoDetail[]>> {
    const options = createRequestOption(req);
    console.log(`${this.resourceUrl}/list`);
    return this.http.get<AmigoDetail[]>(`${this.resourceUrl}/list`, { params: options, observe: 'response' });
  }

  deleteAmigo(amigo: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/eliminar/${amigo}`, { observe: 'response' });
  }

  acceptAmigo(amigo: string): Observable<EntityResponseType> {
    return this.http.post(`${this.resourceUrl}/accept/${amigo}`, null, { observe: 'response' });
  }

  rejectAmigo(amigo: string): Observable<HttpResponse<INotificacion>> {
    return this.http.put<INotificacion>(`${this.resourceUrl}/cancel/${amigo}`, null, { observe: 'response' });
  }

  requestAmigo(amigo: string): Observable<HttpResponse<INotificacion>> {
    return this.http.post<INotificacion>(`${this.resourceUrlNotificacion}/${amigo}`, null, { observe: 'response' });
  }
}
