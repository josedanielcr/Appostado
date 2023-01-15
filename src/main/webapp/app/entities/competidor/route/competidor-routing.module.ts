import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CompetidorComponent } from '../list/competidor.component';
import { CompetidorDetailComponent } from '../detail/competidor-detail.component';
import { CompetidorUpdateComponent } from '../update/competidor-update.component';
import { CompetidorRoutingResolveService } from './competidor-routing-resolve.service';

const competidorRoute: Routes = [
  {
    path: '',
    component: CompetidorComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CompetidorDetailComponent,
    resolve: {
      competidor: CompetidorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CompetidorUpdateComponent,
    resolve: {
      competidor: CompetidorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CompetidorUpdateComponent,
    resolve: {
      competidor: CompetidorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(competidorRoute)],
  exports: [RouterModule],
})
export class CompetidorRoutingModule {}
