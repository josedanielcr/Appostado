import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICuentaUsuario, CuentaUsuario } from '../cuenta-usuario.model';
import { CuentaUsuarioService } from '../service/cuenta-usuario.service';

@Injectable({ providedIn: 'root' })
export class CuentaUsuarioRoutingResolveService implements Resolve<ICuentaUsuario> {
  constructor(protected service: CuentaUsuarioService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICuentaUsuario> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cuentaUsuario: HttpResponse<CuentaUsuario>) => {
          if (cuentaUsuario.body) {
            return of(cuentaUsuario.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CuentaUsuario());
  }
}
