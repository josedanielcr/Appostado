import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MisionComponent } from '../list/mision.component';
import { MisionDetailComponent } from '../detail/mision-detail.component';
import { MisionUpdateComponent } from '../update/mision-update.component';
import { MisionRoutingResolveService } from './mision-routing-resolve.service';

const misionRoute: Routes = [
  {
    path: '',
    component: MisionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MisionDetailComponent,
    resolve: {
      mision: MisionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MisionUpdateComponent,
    resolve: {
      mision: MisionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MisionUpdateComponent,
    resolve: {
      mision: MisionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(misionRoute)],
  exports: [RouterModule],
})
export class MisionRoutingModule {}
