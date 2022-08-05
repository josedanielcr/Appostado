import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApuesta, getApuestaIdentifier } from '../apuesta.model';

export type EntityResponseType = HttpResponse<IApuesta>;
export type EntityArrayResponseType = HttpResponse<IApuesta[]>;

@Injectable({ providedIn: 'root' })
export class ApuestaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/apuestas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(apuesta: IApuesta): Observable<EntityResponseType> {
    return this.http.post<IApuesta>(this.resourceUrl, apuesta, { observe: 'response' });
  }

  update(apuesta: IApuesta): Observable<EntityResponseType> {
    return this.http.put<IApuesta>(`${this.resourceUrl}/${getApuestaIdentifier(apuesta) as number}`, apuesta, { observe: 'response' });
  }

  partialUpdate(apuesta: IApuesta): Observable<EntityResponseType> {
    return this.http.patch<IApuesta>(`${this.resourceUrl}/${getApuestaIdentifier(apuesta) as number}`, apuesta, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IApuesta>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IApuesta[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addApuestaToCollectionIfMissing(apuestaCollection: IApuesta[], ...apuestasToCheck: (IApuesta | null | undefined)[]): IApuesta[] {
    const apuestas: IApuesta[] = apuestasToCheck.filter(isPresent);
    if (apuestas.length > 0) {
      const apuestaCollectionIdentifiers = apuestaCollection.map(apuestaItem => getApuestaIdentifier(apuestaItem)!);
      const apuestasToAdd = apuestas.filter(apuestaItem => {
        const apuestaIdentifier = getApuestaIdentifier(apuestaItem);
        if (apuestaIdentifier == null || apuestaCollectionIdentifiers.includes(apuestaIdentifier)) {
          return false;
        }
        apuestaCollectionIdentifiers.push(apuestaIdentifier);
        return true;
      });
      return [...apuestasToAdd, ...apuestaCollection];
    }
    return apuestaCollection;
  }

  getApuestasByEventId(eventId: string | null): Observable<EntityArrayResponseType> {
    return this.http.get<IApuesta[]>(`${this.resourceUrl}/evento/${eventId!}`, { observe: 'response' });
  }
}
