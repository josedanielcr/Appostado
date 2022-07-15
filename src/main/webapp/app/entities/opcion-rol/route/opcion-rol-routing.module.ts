import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OpcionRolComponent } from '../list/opcion-rol.component';
import { OpcionRolDetailComponent } from '../detail/opcion-rol-detail.component';
import { OpcionRolUpdateComponent } from '../update/opcion-rol-update.component';
import { OpcionRolRoutingResolveService } from './opcion-rol-routing-resolve.service';

const opcionRolRoute: Routes = [
  {
    path: '',
    component: OpcionRolComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OpcionRolDetailComponent,
    resolve: {
      opcionRol: OpcionRolRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OpcionRolUpdateComponent,
    resolve: {
      opcionRol: OpcionRolRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OpcionRolUpdateComponent,
    resolve: {
      opcionRol: OpcionRolRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(opcionRolRoute)],
  exports: [RouterModule],
})
export class OpcionRolRoutingModule {}
