import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { QuinielaComponent } from '../list/quiniela.component';
import { QuinielaDetailComponent } from '../detail/quiniela-detail.component';
import { QuinielaUpdateComponent } from '../update/quiniela-update.component';
import { QuinielaRoutingResolveService } from './quiniela-routing-resolve.service';

const quinielaRoute: Routes = [
  {
    path: '',
    component: QuinielaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QuinielaDetailComponent,
    resolve: {
      quiniela: QuinielaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QuinielaUpdateComponent,
    resolve: {
      quiniela: QuinielaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QuinielaUpdateComponent,
    resolve: {
      quiniela: QuinielaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(quinielaRoute)],
  exports: [RouterModule],
})
export class QuinielaRoutingModule {}
