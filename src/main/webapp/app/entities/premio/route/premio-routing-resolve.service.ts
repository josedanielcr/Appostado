import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPremio, Premio } from '../premio.model';
import { PremioService } from '../service/premio.service';

@Injectable({ providedIn: 'root' })
export class PremioRoutingResolveService implements Resolve<IPremio> {
  constructor(protected service: PremioService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPremio> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((premio: HttpResponse<Premio>) => {
          if (premio.body) {
            return of(premio.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Premio());
  }
}
