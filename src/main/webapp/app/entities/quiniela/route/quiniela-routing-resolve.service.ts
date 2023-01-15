import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQuiniela, Quiniela } from '../quiniela.model';
import { QuinielaService } from '../service/quiniela.service';

@Injectable({ providedIn: 'root' })
export class QuinielaRoutingResolveService implements Resolve<IQuiniela> {
  constructor(protected service: QuinielaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IQuiniela> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((quiniela: HttpResponse<Quiniela>) => {
          if (quiniela.body) {
            return of(quiniela.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Quiniela());
  }
}
