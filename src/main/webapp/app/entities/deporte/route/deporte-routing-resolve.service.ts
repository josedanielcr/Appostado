import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDeporte, Deporte } from '../deporte.model';
import { DeporteService } from '../service/deporte.service';

@Injectable({ providedIn: 'root' })
export class DeporteRoutingResolveService implements Resolve<IDeporte> {
  constructor(protected service: DeporteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDeporte> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((deporte: HttpResponse<Deporte>) => {
          if (deporte.body) {
            return of(deporte.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Deporte());
  }
}
