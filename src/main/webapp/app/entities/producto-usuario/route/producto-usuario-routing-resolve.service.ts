import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductoUsuario, ProductoUsuario } from '../producto-usuario.model';
import { ProductoUsuarioService } from '../service/producto-usuario.service';

@Injectable({ providedIn: 'root' })
export class ProductoUsuarioRoutingResolveService implements Resolve<IProductoUsuario> {
  constructor(protected service: ProductoUsuarioService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProductoUsuario> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((productoUsuario: HttpResponse<ProductoUsuario>) => {
          if (productoUsuario.body) {
            return of(productoUsuario.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ProductoUsuario());
  }
}
