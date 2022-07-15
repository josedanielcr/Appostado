import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PremioComponent } from '../list/premio.component';
import { PremioDetailComponent } from '../detail/premio-detail.component';
import { PremioUpdateComponent } from '../update/premio-update.component';
import { PremioRoutingResolveService } from './premio-routing-resolve.service';

const premioRoute: Routes = [
  {
    path: '',
    component: PremioComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PremioDetailComponent,
    resolve: {
      premio: PremioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PremioUpdateComponent,
    resolve: {
      premio: PremioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PremioUpdateComponent,
    resolve: {
      premio: PremioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(premioRoute)],
  exports: [RouterModule],
})
export class PremioRoutingModule {}
