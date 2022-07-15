import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ApuestaComponent } from '../list/apuesta.component';
import { ApuestaDetailComponent } from '../detail/apuesta-detail.component';
import { ApuestaUpdateComponent } from '../update/apuesta-update.component';
import { ApuestaRoutingResolveService } from './apuesta-routing-resolve.service';

const apuestaRoute: Routes = [
  {
    path: '',
    component: ApuestaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApuestaDetailComponent,
    resolve: {
      apuesta: ApuestaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApuestaUpdateComponent,
    resolve: {
      apuesta: ApuestaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApuestaUpdateComponent,
    resolve: {
      apuesta: ApuestaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(apuestaRoute)],
  exports: [RouterModule],
})
export class ApuestaRoutingModule {}
