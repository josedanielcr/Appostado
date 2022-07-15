import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMision, getMisionIdentifier } from '../mision.model';

export type EntityResponseType = HttpResponse<IMision>;
export type EntityArrayResponseType = HttpResponse<IMision[]>;

@Injectable({ providedIn: 'root' })
export class MisionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/misions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(mision: IMision): Observable<EntityResponseType> {
    return this.http.post<IMision>(this.resourceUrl, mision, { observe: 'response' });
  }

  update(mision: IMision): Observable<EntityResponseType> {
    return this.http.put<IMision>(`${this.resourceUrl}/${getMisionIdentifier(mision) as number}`, mision, { observe: 'response' });
  }

  partialUpdate(mision: IMision): Observable<EntityResponseType> {
    return this.http.patch<IMision>(`${this.resourceUrl}/${getMisionIdentifier(mision) as number}`, mision, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMision>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMision[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMisionToCollectionIfMissing(misionCollection: IMision[], ...misionsToCheck: (IMision | null | undefined)[]): IMision[] {
    const misions: IMision[] = misionsToCheck.filter(isPresent);
    if (misions.length > 0) {
      const misionCollectionIdentifiers = misionCollection.map(misionItem => getMisionIdentifier(misionItem)!);
      const misionsToAdd = misions.filter(misionItem => {
        const misionIdentifier = getMisionIdentifier(misionItem);
        if (misionIdentifier == null || misionCollectionIdentifiers.includes(misionIdentifier)) {
          return false;
        }
        misionCollectionIdentifiers.push(misionIdentifier);
        return true;
      });
      return [...misionsToAdd, ...misionCollection];
    }
    return misionCollection;
  }
}
