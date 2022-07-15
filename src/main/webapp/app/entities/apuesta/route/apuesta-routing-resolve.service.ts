import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApuesta, Apuesta } from '../apuesta.model';
import { ApuestaService } from '../service/apuesta.service';

@Injectable({ providedIn: 'root' })
export class ApuestaRoutingResolveService implements Resolve<IApuesta> {
  constructor(protected service: ApuestaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IApuesta> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((apuesta: HttpResponse<Apuesta>) => {
          if (apuesta.body) {
            return of(apuesta.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Apuesta());
  }
}
