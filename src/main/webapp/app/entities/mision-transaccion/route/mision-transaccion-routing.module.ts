import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MisionTransaccionComponent } from '../list/mision-transaccion.component';
import { MisionTransaccionDetailComponent } from '../detail/mision-transaccion-detail.component';
import { MisionTransaccionUpdateComponent } from '../update/mision-transaccion-update.component';
import { MisionTransaccionRoutingResolveService } from './mision-transaccion-routing-resolve.service';

const misionTransaccionRoute: Routes = [
  {
    path: '',
    component: MisionTransaccionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MisionTransaccionDetailComponent,
    resolve: {
      misionTransaccion: MisionTransaccionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MisionTransaccionUpdateComponent,
    resolve: {
      misionTransaccion: MisionTransaccionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MisionTransaccionUpdateComponent,
    resolve: {
      misionTransaccion: MisionTransaccionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(misionTransaccionRoute)],
  exports: [RouterModule],
})
export class MisionTransaccionRoutingModule {}
