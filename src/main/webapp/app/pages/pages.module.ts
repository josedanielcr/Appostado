import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PagesComponent } from './pages.component';
import { PanelComponent } from './panel/panel.component';
import { AppRoutingModule } from '../app-routing.module';
import { SidebarComponent } from '../layouts/sidebar/sidebar.component';
import { NavbarComponent } from '../layouts/navbar/navbar.component';
import { fontAwesomeIcons } from '../config/font-awesome-icons';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbCollapseModule } from '@ng-bootstrap/ng-bootstrap';
import { SharedModule } from '../shared/shared.module';
import { ApuestasPageComponent } from './apuestas-page/apuestas-page.component';
import { QuinielasPageComponent } from './quinielas-page/quinielas-page.component';
import { PremiosPageComponent } from './premios-page/premios-page.component';
import { ProductosPageComponent } from './productos-page/productos-page.component';
import { RankingPageComponent } from './ranking-page/ranking-page.component';
import { TransaccionesPageComponent } from './transacciones-page/transacciones-page.component';
import { ConfiguracionesPageComponent } from './configuraciones-page/configuraciones-page.component';
import { HistorialApuestasPageComponent } from './historial-apuestas-page/historial-apuestas-page.component';
import { BalancePageComponent } from './balance-page/balance-page.component';
import { CuentaPageComponent } from './cuenta-page/cuenta-page.component';
import { ApuestasAdminPageComponent } from './apuestas-admin-page/apuestas-admin-page.component';
import { QuinielasAdminPageComponent } from './quinielas-admin-page/quinielas-admin-page.component';
import { PremiosAdminPageComponent } from './premios-admin-page/premios-admin-page.component';
import { ProductosAdminPageComponent } from './productos-admin-page/productos-admin-page.component';
import { UsuariosAdminPageComponent } from './usuarios-admin-page/usuarios-admin-page.component';
import { RankingAdminPageComponent } from './ranking-admin-page/ranking-admin-page.component';
import { AmigosPageComponent } from './amigos-page/amigos-page.component';
import { LigasPageComponent } from './ligas-page/ligas-page.component';
import { PasswordPageComponent } from './password-page/password-page.component';
import { ParametroModule } from '../entities/parametro/parametro.module';
import { DeporteModule } from '../entities/deporte/deporte.module';
import { DivisionModule } from '../entities/division/division.module';
import { CompetidorModule } from '../entities/competidor/competidor.module';
import { EventoModule } from '../entities/evento/evento.module';
import { AccountModule } from '../account/account.module';
import { DataTablesModule } from 'angular-datatables';
import { TableModule } from 'primeng/table';
import { DropdownModule } from 'primeng/dropdown';
import { SliderModule } from 'primeng/slider';
import { ProgressBarModule } from 'primeng/progressbar';

@NgModule({
  declarations: [
    PagesComponent,
    PanelComponent,
    SidebarComponent,
    NavbarComponent,
    ApuestasPageComponent,
    QuinielasPageComponent,
    PremiosPageComponent,
    ProductosPageComponent,
    RankingPageComponent,
    TransaccionesPageComponent,
    ConfiguracionesPageComponent,
    HistorialApuestasPageComponent,
    BalancePageComponent,
    CuentaPageComponent,
    ApuestasAdminPageComponent,
    QuinielasAdminPageComponent,
    PremiosAdminPageComponent,
    ProductosAdminPageComponent,
    UsuariosAdminPageComponent,
    RankingAdminPageComponent,
    AmigosPageComponent,
    LigasPageComponent,
    PasswordPageComponent,
  ],
  providers: [FaIconLibrary],
  imports: [
    CommonModule,
    AppRoutingModule,
    FontAwesomeModule,
    NgbCollapseModule,
    SharedModule,
    ParametroModule,
    DeporteModule,
    DivisionModule,
    CompetidorModule,
    EventoModule,
    AccountModule,
    DataTablesModule,
    TableModule,
    DropdownModule,
    SliderModule,
    ProgressBarModule,
  ],
})
export class PagesModule {
  constructor(iconLibrary: FaIconLibrary) {
    iconLibrary.addIcons(...fontAwesomeIcons);
  }
}
