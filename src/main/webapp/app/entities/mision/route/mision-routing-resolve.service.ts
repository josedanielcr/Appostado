import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMision, Mision } from '../mision.model';
import { MisionService } from '../service/mision.service';

@Injectable({ providedIn: 'root' })
export class MisionRoutingResolveService implements Resolve<IMision> {
  constructor(protected service: MisionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMision> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((mision: HttpResponse<Mision>) => {
          if (mision.body) {
            return of(mision.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Mision());
  }
}
