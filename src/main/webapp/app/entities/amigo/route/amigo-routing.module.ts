import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AmigoComponent } from '../list/amigo.component';
import { AmigoDetailComponent } from '../detail/amigo-detail.component';
import { AmigoUpdateComponent } from '../update/amigo-update.component';
import { AmigoRoutingResolveService } from './amigo-routing-resolve.service';

const amigoRoute: Routes = [
  {
    path: '',
    component: AmigoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AmigoDetailComponent,
    resolve: {
      amigo: AmigoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AmigoUpdateComponent,
    resolve: {
      amigo: AmigoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AmigoUpdateComponent,
    resolve: {
      amigo: AmigoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(amigoRoute)],
  exports: [RouterModule],
})
export class AmigoRoutingModule {}
