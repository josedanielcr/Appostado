import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICanje, Canje } from '../canje.model';
import { CanjeService } from '../service/canje.service';

@Injectable({ providedIn: 'root' })
export class CanjeRoutingResolveService implements Resolve<ICanje> {
  constructor(protected service: CanjeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICanje> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((canje: HttpResponse<Canje>) => {
          if (canje.body) {
            return of(canje.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Canje());
  }
}
