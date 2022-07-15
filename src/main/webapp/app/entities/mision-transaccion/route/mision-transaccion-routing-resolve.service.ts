import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMisionTransaccion, MisionTransaccion } from '../mision-transaccion.model';
import { MisionTransaccionService } from '../service/mision-transaccion.service';

@Injectable({ providedIn: 'root' })
export class MisionTransaccionRoutingResolveService implements Resolve<IMisionTransaccion> {
  constructor(protected service: MisionTransaccionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMisionTransaccion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((misionTransaccion: HttpResponse<MisionTransaccion>) => {
          if (misionTransaccion.body) {
            return of(misionTransaccion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MisionTransaccion());
  }
}
