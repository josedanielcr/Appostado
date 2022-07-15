import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITransaccion, Transaccion } from '../transaccion.model';
import { TransaccionService } from '../service/transaccion.service';

@Injectable({ providedIn: 'root' })
export class TransaccionRoutingResolveService implements Resolve<ITransaccion> {
  constructor(protected service: TransaccionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITransaccion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((transaccion: HttpResponse<Transaccion>) => {
          if (transaccion.body) {
            return of(transaccion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Transaccion());
  }
}
