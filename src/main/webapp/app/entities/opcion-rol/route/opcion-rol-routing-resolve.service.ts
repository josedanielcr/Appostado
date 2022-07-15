import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOpcionRol, OpcionRol } from '../opcion-rol.model';
import { OpcionRolService } from '../service/opcion-rol.service';

@Injectable({ providedIn: 'root' })
export class OpcionRolRoutingResolveService implements Resolve<IOpcionRol> {
  constructor(protected service: OpcionRolService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOpcionRol> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((opcionRol: HttpResponse<OpcionRol>) => {
          if (opcionRol.body) {
            return of(opcionRol.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OpcionRol());
  }
}
