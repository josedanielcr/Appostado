import { HttpClient, HttpResponse } from '@angular/common/http';
import { ITransaccion } from '../../entities/transaccion/transaccion.model';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { createRequestOption } from '../../core/request/request-util';
import dayjs from 'dayjs/esm';

export type EntityResponseType = HttpResponse<ITransaccion>;
export type EntityArrayResponseType = HttpResponse<ITransaccion[]>;

@Injectable({ providedIn: 'root' })
export class BalancePageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/transaccions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getTransacciones(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITransaccion[]>(`${this.resourceUrl}/user`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((transaccion: ITransaccion) => {
        transaccion.fecha = transaccion.fecha ? dayjs(transaccion.fecha) : undefined;
      });
    }
    return res;
  }
}
