import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMisionUsuario, MisionUsuario } from '../mision-usuario.model';
import { MisionUsuarioService } from '../service/mision-usuario.service';

@Injectable({ providedIn: 'root' })
export class MisionUsuarioRoutingResolveService implements Resolve<IMisionUsuario> {
  constructor(protected service: MisionUsuarioService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMisionUsuario> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((misionUsuario: HttpResponse<MisionUsuario>) => {
          if (misionUsuario.body) {
            return of(misionUsuario.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MisionUsuario());
  }
}
