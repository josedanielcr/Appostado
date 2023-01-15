import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LigaComponent } from '../list/liga.component';
import { LigaDetailComponent } from '../detail/liga-detail.component';
import { LigaUpdateComponent } from '../update/liga-update.component';
import { LigaRoutingResolveService } from './liga-routing-resolve.service';

const ligaRoute: Routes = [
  {
    path: '',
    component: LigaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LigaDetailComponent,
    resolve: {
      liga: LigaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LigaUpdateComponent,
    resolve: {
      liga: LigaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LigaUpdateComponent,
    resolve: {
      liga: LigaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ligaRoute)],
  exports: [RouterModule],
})
export class LigaRoutingModule {}
