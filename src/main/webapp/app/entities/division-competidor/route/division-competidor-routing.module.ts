import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DivisionCompetidorComponent } from '../list/division-competidor.component';
import { DivisionCompetidorDetailComponent } from '../detail/division-competidor-detail.component';
import { DivisionCompetidorUpdateComponent } from '../update/division-competidor-update.component';
import { DivisionCompetidorRoutingResolveService } from './division-competidor-routing-resolve.service';

const divisionCompetidorRoute: Routes = [
  {
    path: '',
    component: DivisionCompetidorComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DivisionCompetidorDetailComponent,
    resolve: {
      divisionCompetidor: DivisionCompetidorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DivisionCompetidorUpdateComponent,
    resolve: {
      divisionCompetidor: DivisionCompetidorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DivisionCompetidorUpdateComponent,
    resolve: {
      divisionCompetidor: DivisionCompetidorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(divisionCompetidorRoute)],
  exports: [RouterModule],
})
export class DivisionCompetidorRoutingModule {}
