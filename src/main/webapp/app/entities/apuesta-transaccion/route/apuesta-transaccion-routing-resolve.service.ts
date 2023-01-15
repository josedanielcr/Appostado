import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApuestaTransaccion, ApuestaTransaccion } from '../apuesta-transaccion.model';
import { ApuestaTransaccionService } from '../service/apuesta-transaccion.service';

@Injectable({ providedIn: 'root' })
export class ApuestaTransaccionRoutingResolveService implements Resolve<IApuestaTransaccion> {
  constructor(protected service: ApuestaTransaccionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IApuestaTransaccion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((apuestaTransaccion: HttpResponse<ApuestaTransaccion>) => {
          if (apuestaTransaccion.body) {
            return of(apuestaTransaccion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ApuestaTransaccion());
  }
}
