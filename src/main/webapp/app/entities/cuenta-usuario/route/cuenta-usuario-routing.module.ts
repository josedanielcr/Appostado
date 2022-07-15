import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CuentaUsuarioComponent } from '../list/cuenta-usuario.component';
import { CuentaUsuarioDetailComponent } from '../detail/cuenta-usuario-detail.component';
import { CuentaUsuarioUpdateComponent } from '../update/cuenta-usuario-update.component';
import { CuentaUsuarioRoutingResolveService } from './cuenta-usuario-routing-resolve.service';

const cuentaUsuarioRoute: Routes = [
  {
    path: '',
    component: CuentaUsuarioComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CuentaUsuarioDetailComponent,
    resolve: {
      cuentaUsuario: CuentaUsuarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CuentaUsuarioUpdateComponent,
    resolve: {
      cuentaUsuario: CuentaUsuarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CuentaUsuarioUpdateComponent,
    resolve: {
      cuentaUsuario: CuentaUsuarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cuentaUsuarioRoute)],
  exports: [RouterModule],
})
export class CuentaUsuarioRoutingModule {}
