import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAmigo, Amigo } from '../amigo.model';
import { AmigoService } from '../service/amigo.service';

@Injectable({ providedIn: 'root' })
export class AmigoRoutingResolveService implements Resolve<IAmigo> {
  constructor(protected service: AmigoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAmigo> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((amigo: HttpResponse<Amigo>) => {
          if (amigo.body) {
            return of(amigo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Amigo());
  }
}
