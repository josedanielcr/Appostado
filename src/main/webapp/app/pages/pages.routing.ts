import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { PagesComponent } from './pages.component';
import { PanelComponent } from './panel/panel.component';

const routes: Routes = [
  {
    path: 'panel',
    component: PagesComponent,
    children: [{ path: '', component: PanelComponent, data: { title: 'Panel' } }],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PagesRoutingModule {}
