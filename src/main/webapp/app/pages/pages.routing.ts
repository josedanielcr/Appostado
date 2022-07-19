import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { PagesComponent } from './pages.component';
import { PanelComponent } from './panel/panel.component';

const routes: Routes = [
  {
    path: 'panel',
    component: PagesComponent,
    children: [
      { path: '', component: PanelComponent, data: { title: 'Panel' } },

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
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PagesRoutingModule {}
