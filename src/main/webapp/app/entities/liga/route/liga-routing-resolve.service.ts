import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILiga, Liga } from '../liga.model';
import { LigaService } from '../service/liga.service';

@Injectable({ providedIn: 'root' })
export class LigaRoutingResolveService implements Resolve<ILiga> {
  constructor(protected service: LigaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILiga> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((liga: HttpResponse<Liga>) => {
          if (liga.body) {
            return of(liga.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Liga());
  }
}
