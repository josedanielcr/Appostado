import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ApuestaTransaccionComponent } from '../list/apuesta-transaccion.component';
import { ApuestaTransaccionDetailComponent } from '../detail/apuesta-transaccion-detail.component';
import { ApuestaTransaccionUpdateComponent } from '../update/apuesta-transaccion-update.component';
import { ApuestaTransaccionRoutingResolveService } from './apuesta-transaccion-routing-resolve.service';

const apuestaTransaccionRoute: Routes = [
  {
    path: '',
    component: ApuestaTransaccionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ApuestaTransaccionDetailComponent,
    resolve: {
      apuestaTransaccion: ApuestaTransaccionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ApuestaTransaccionUpdateComponent,
    resolve: {
      apuestaTransaccion: ApuestaTransaccionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ApuestaTransaccionUpdateComponent,
    resolve: {
      apuestaTransaccion: ApuestaTransaccionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(apuestaTransaccionRoute)],
  exports: [RouterModule],
})
export class ApuestaTransaccionRoutingModule {}
