import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICompetidor, Competidor } from '../competidor.model';
import { CompetidorService } from '../service/competidor.service';

@Injectable({ providedIn: 'root' })
export class CompetidorRoutingResolveService implements Resolve<ICompetidor> {
  constructor(protected service: CompetidorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICompetidor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((competidor: HttpResponse<Competidor>) => {
          if (competidor.body) {
            return of(competidor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Competidor());
  }
}
