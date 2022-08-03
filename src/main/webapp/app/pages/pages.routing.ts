import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { PagesComponent } from './pages.component';
import { PanelComponent } from './panel/panel.component';
import { BalancePageComponent } from './balance-page/balance-page.component';
import { ApuestasPageComponent } from './apuestas-page/apuestas-page.component';
import { ConfiguracionesPageComponent } from './configuraciones-page/configuraciones-page.component';
import { CuentaPageComponent } from './cuenta-page/cuenta-page.component';
import { HistorialApuestasPageComponent } from './historial-apuestas-page/historial-apuestas-page.component';
import { PremiosPageComponent } from './premios-page/premios-page.component';
import { ProductosPageComponent } from './productos-page/productos-page.component';
import { QuinielasPageComponent } from './quinielas-page/quinielas-page.component';
import { RankingPageComponent } from './ranking-page/ranking-page.component';
import { TransaccionesPageComponent } from './transacciones-page/transacciones-page.component';
import { ApuestasAdminPageComponent } from './apuestas-admin-page/apuestas-admin-page.component';
import { QuinielasAdminPageComponent } from './quinielas-admin-page/quinielas-admin-page.component';
import { PremiosAdminPageComponent } from './premios-admin-page/premios-admin-page.component';
import { ProductosAdminPageComponent } from './productos-admin-page/productos-admin-page.component';
import { RankingAdminPageComponent } from './ranking-admin-page/ranking-admin-page.component';
import { LigasPageComponent } from './ligas-page/ligas-page.component';
import { AmigosPageComponent } from './amigos-page/amigos-page.component';
import { PasswordPageComponent } from './password-page/password-page.component';
import { Authority } from '../config/authority.constants';

const routes: Routes = [
  {
    path: 'panel',
    data: {
      authorities: [Authority.USER, Authority.ADMIN],
    },
    component: PagesComponent,
    children: [
      { path: '', component: PanelComponent, data: { title: 'Panel' } },
      { path: 'apuestas', component: ApuestasPageComponent, data: { title: 'Apuestas' } },
      { path: 'balance', component: BalancePageComponent, data: { title: 'Balance' } },
      { path: 'configuraciones', component: ConfiguracionesPageComponent, data: { title: 'Configuraciones' } },
      { path: 'cuenta', component: CuentaPageComponent, data: { title: 'Cuenta' } },
      { path: 'historialApuestas', component: HistorialApuestasPageComponent, data: { title: 'Historial de apuestas' } },
      { path: 'premios', component: PremiosPageComponent, data: { title: 'Premios' } },
      { path: 'productos', component: ProductosPageComponent, data: { title: 'Productos' } },
      { path: 'quinielas', component: QuinielasPageComponent, data: { title: 'Quinielas' } },
      { path: 'ranking', component: RankingPageComponent, data: { title: 'Ranking' } },
      { path: 'transacciones', component: TransaccionesPageComponent, data: { title: 'Transacciones' } },
      { path: 'ligas', component: LigasPageComponent, data: { title: 'Ligas' } },
      { path: 'amigos', component: AmigosPageComponent, data: { title: 'Amigos' } },
      { path: 'adminapuestas', component: ApuestasAdminPageComponent, data: { title: 'gestión de apuestas' } },
      { path: 'adminquinielas', component: QuinielasAdminPageComponent, data: { title: 'gestión de quinielas' } },
      { path: 'adminpremios', component: PremiosAdminPageComponent, data: { title: 'gestión de premios' } },
      { path: 'adminproductos', component: ProductosAdminPageComponent, data: { title: 'gestión de productos' } },
      { path: 'adminranking', component: RankingAdminPageComponent, data: { title: 'gestión de ranking' } },
      { path: 'password', component: PasswordPageComponent, data: { title: 'password' } },
      {
        path: 'parametro',
        data: { pageTitle: 'appostadoApp.parametro.home.title' },
        loadChildren: () => import('../entities/parametro/parametro.module').then(m => m.ParametroModule),
      },
      {
        path: 'competidor',
        data: { pageTitle: 'appostadoApp.competidor.home.title' },
        loadChildren: () => import('../entities/competidor/competidor.module').then(m => m.CompetidorModule),
      },
      {
        path: 'evento',
        data: { pageTitle: 'appostadoApp.evento.home.title' },
        loadChildren: () => import('../entities/evento/evento.module').then(m => m.EventoModule),
      },
      {
        path: 'deporte',
        data: { pageTitle: 'appostadoApp.deporte.home.title' },
        loadChildren: () => import('../entities/deporte/deporte.module').then(m => m.DeporteModule),
      },
      {
        path: 'division',
        data: { pageTitle: 'appostadoApp.division.home.title' },
        loadChildren: () => import('../entities/division/division.module').then(m => m.DivisionModule),
      },
      {
        path: 'division-competidor',
        data: { pageTitle: 'appostadoApp.divisionCompetidor.home.title' },
        loadChildren: () => import('../entities/division-competidor/division-competidor.module').then(m => m.DivisionCompetidorModule),
      },

      {
        path: 'premio',
        data: { pageTitle: 'appostadoApp.gestionPremios.home.title' },
        loadChildren: () => import('../entities/premio/premio.module').then(m => m.PremioModule),
      },

      {
        path: 'producto',
        data: { pageTitle: 'appostadoApp.gestionProducto.home.title' },
        loadChildren: () => import('../entities/producto/producto.module').then(m => m.ProductoModule),
      },

      {
        path: 'canje',
        data: { pageTitle: 'appostadoApp.gestionCanje.home.title' },
        loadChildren: () => import('../entities/canje/canje.module').then(m => m.CanjeModule),
      },
      {
        path: 'perfil',
        data: { pageTitle: 'Appostados' },
        loadChildren: () => import('../entities/usuario/usuario.module').then(m => m.UsuarioModule),
      },
      {
        path: 'adminusuarios',
        data: { pageTitle: 'Appostados' },
        loadChildren: () => import('../admin/user-management/user-management.module').then(m => m.UserManagementModule),
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PagesRoutingModule {}
