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

@NgModule({
  declarations: [PagesComponent, PanelComponent, SidebarComponent, NavbarComponent],
  providers: [FaIconLibrary],
  imports: [CommonModule, AppRoutingModule, FontAwesomeModule, NgbCollapseModule, SharedModule],
})
export class PagesModule {
  constructor(iconLibrary: FaIconLibrary) {
    iconLibrary.addIcons(...fontAwesomeIcons);
  }
}
