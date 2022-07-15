import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DeporteComponent } from '../list/deporte.component';
import { DeporteDetailComponent } from '../detail/deporte-detail.component';
import { DeporteUpdateComponent } from '../update/deporte-update.component';
import { DeporteRoutingResolveService } from './deporte-routing-resolve.service';

const deporteRoute: Routes = [
  {
    path: '',
    component: DeporteComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeporteDetailComponent,
    resolve: {
      deporte: DeporteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DeporteUpdateComponent,
    resolve: {
      deporte: DeporteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DeporteUpdateComponent,
    resolve: {
      deporte: DeporteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(deporteRoute)],
  exports: [RouterModule],
})
export class DeporteRoutingModule {}
