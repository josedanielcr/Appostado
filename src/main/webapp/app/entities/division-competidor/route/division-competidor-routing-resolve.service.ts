import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDivisionCompetidor, DivisionCompetidor } from '../division-competidor.model';
import { DivisionCompetidorService } from '../service/division-competidor.service';

@Injectable({ providedIn: 'root' })
export class DivisionCompetidorRoutingResolveService implements Resolve<IDivisionCompetidor> {
  constructor(protected service: DivisionCompetidorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDivisionCompetidor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((divisionCompetidor: HttpResponse<DivisionCompetidor>) => {
          if (divisionCompetidor.body) {
            return of(divisionCompetidor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DivisionCompetidor());
  }
}
