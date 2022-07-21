import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { errorRoute } from './layouts/error/error.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { Authority } from 'app/config/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PagesRoutingModule } from './pages/pages.routing';
import { LandingComponent } from './landing/landing.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/panel',
    pathMatch: 'full',
  },
  {
    path: 'admin',
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
    loadChildren: () => import('./admin/admin-routing.module').then(m => m.AdminRoutingModule),
  },
  {
    path: 'account',
    loadChildren: () => import('./account/account.module').then(m => m.AccountModule),
  },
  {
    path: 'login',
    loadChildren: () => import('./login/login.module').then(m => m.LoginModule),
  },

  { path: 'landing', component: LandingComponent, data: { title: 'Landing' } },

  ...errorRoute,
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { enableTracing: DEBUG_INFO_ENABLED }), PagesRoutingModule],
  exports: [RouterModule],
})
export class AppRoutingModule {}
