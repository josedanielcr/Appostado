import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductoUsuarioComponent } from '../list/producto-usuario.component';
import { ProductoUsuarioDetailComponent } from '../detail/producto-usuario-detail.component';
import { ProductoUsuarioUpdateComponent } from '../update/producto-usuario-update.component';
import { ProductoUsuarioRoutingResolveService } from './producto-usuario-routing-resolve.service';

const productoUsuarioRoute: Routes = [
  {
    path: '',
    component: ProductoUsuarioComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductoUsuarioDetailComponent,
    resolve: {
      productoUsuario: ProductoUsuarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductoUsuarioUpdateComponent,
    resolve: {
      productoUsuario: ProductoUsuarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductoUsuarioUpdateComponent,
    resolve: {
      productoUsuario: ProductoUsuarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(productoUsuarioRoute)],
  exports: [RouterModule],
})
export class ProductoUsuarioRoutingModule {}
