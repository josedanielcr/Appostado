import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IParametro, Parametro } from '../parametro.model';
import { ParametroService } from '../service/parametro.service';

@Injectable({ providedIn: 'root' })
export class ParametroRoutingResolveService implements Resolve<IParametro> {
  constructor(protected service: ParametroService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IParametro> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((parametro: HttpResponse<Parametro>) => {
          if (parametro.body) {
            return of(parametro.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Parametro());
  }
}
