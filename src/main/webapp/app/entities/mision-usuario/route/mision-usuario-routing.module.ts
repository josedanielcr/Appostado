import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MisionUsuarioComponent } from '../list/mision-usuario.component';
import { MisionUsuarioDetailComponent } from '../detail/mision-usuario-detail.component';
import { MisionUsuarioUpdateComponent } from '../update/mision-usuario-update.component';
import { MisionUsuarioRoutingResolveService } from './mision-usuario-routing-resolve.service';

const misionUsuarioRoute: Routes = [
  {
    path: '',
    component: MisionUsuarioComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MisionUsuarioDetailComponent,
    resolve: {
      misionUsuario: MisionUsuarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MisionUsuarioUpdateComponent,
    resolve: {
      misionUsuario: MisionUsuarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MisionUsuarioUpdateComponent,
    resolve: {
      misionUsuario: MisionUsuarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(misionUsuarioRoute)],
  exports: [RouterModule],
})
export class MisionUsuarioRoutingModule {}
