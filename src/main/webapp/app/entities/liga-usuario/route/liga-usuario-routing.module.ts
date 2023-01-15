import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LigaUsuarioComponent } from '../list/liga-usuario.component';
import { LigaUsuarioDetailComponent } from '../detail/liga-usuario-detail.component';
import { LigaUsuarioUpdateComponent } from '../update/liga-usuario-update.component';
import { LigaUsuarioRoutingResolveService } from './liga-usuario-routing-resolve.service';

const ligaUsuarioRoute: Routes = [
  {
    path: '',
    component: LigaUsuarioComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LigaUsuarioDetailComponent,
    resolve: {
      ligaUsuario: LigaUsuarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LigaUsuarioUpdateComponent,
    resolve: {
      ligaUsuario: LigaUsuarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LigaUsuarioUpdateComponent,
    resolve: {
      ligaUsuario: LigaUsuarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ligaUsuarioRoute)],
  exports: [RouterModule],
})
export class LigaUsuarioRoutingModule {}
