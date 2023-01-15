import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CanjeComponent } from '../list/canje.component';
import { CanjeDetailComponent } from '../detail/canje-detail.component';
import { CanjeUpdateComponent } from '../update/canje-update.component';
import { CanjeRoutingResolveService } from './canje-routing-resolve.service';

const canjeRoute: Routes = [
  {
    path: '',
    component: CanjeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CanjeDetailComponent,
    resolve: {
      canje: CanjeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CanjeUpdateComponent,
    resolve: {
      canje: CanjeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CanjeUpdateComponent,
    resolve: {
      canje: CanjeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(canjeRoute)],
  exports: [RouterModule],
})
export class CanjeRoutingModule {}
