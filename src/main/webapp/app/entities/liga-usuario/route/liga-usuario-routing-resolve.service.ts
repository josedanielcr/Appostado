import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILigaUsuario, LigaUsuario } from '../liga-usuario.model';
import { LigaUsuarioService } from '../service/liga-usuario.service';

@Injectable({ providedIn: 'root' })
export class LigaUsuarioRoutingResolveService implements Resolve<ILigaUsuario> {
  constructor(protected service: LigaUsuarioService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILigaUsuario> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ligaUsuario: HttpResponse<LigaUsuario>) => {
          if (ligaUsuario.body) {
            return of(ligaUsuario.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LigaUsuario());
  }
}
