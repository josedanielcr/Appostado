import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ParametroComponent } from '../list/parametro.component';
import { ParametroDetailComponent } from '../detail/parametro-detail.component';
import { ParametroUpdateComponent } from '../update/parametro-update.component';
import { ParametroRoutingResolveService } from './parametro-routing-resolve.service';

const parametroRoute: Routes = [
  {
    path: '',
    component: ParametroComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ParametroDetailComponent,
    resolve: {
      parametro: ParametroRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ParametroUpdateComponent,
    resolve: {
      parametro: ParametroRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ParametroUpdateComponent,
    resolve: {
      parametro: ParametroRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(parametroRoute)],
  exports: [RouterModule],
})
export class ParametroRoutingModule {}
