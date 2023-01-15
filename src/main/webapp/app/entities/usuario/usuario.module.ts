import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { UsuarioComponent } from './list/usuario.component';
import { UsuarioDetailComponent } from './detail/usuario-detail.component';
import { UsuarioUpdateComponent } from './update/usuario-update.component';
import { UsuarioDeleteDialogComponent } from './delete/usuario-delete-dialog.component';
import { UsuarioRoutingModule } from './route/usuario-routing.module';
import { ToolbarModule } from 'primeng/toolbar';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';

@NgModule({
  imports: [SharedModule, UsuarioRoutingModule, ToolbarModule, TableModule, ButtonModule, RippleModule],
  declarations: [UsuarioComponent, UsuarioDetailComponent, UsuarioUpdateComponent, UsuarioDeleteDialogComponent],
  entryComponents: [UsuarioDeleteDialogComponent],
})
export class UsuarioModule {}
