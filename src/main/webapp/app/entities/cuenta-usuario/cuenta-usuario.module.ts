import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CuentaUsuarioComponent } from './list/cuenta-usuario.component';
import { CuentaUsuarioDetailComponent } from './detail/cuenta-usuario-detail.component';
import { CuentaUsuarioUpdateComponent } from './update/cuenta-usuario-update.component';
import { CuentaUsuarioDeleteDialogComponent } from './delete/cuenta-usuario-delete-dialog.component';
import { CuentaUsuarioRoutingModule } from './route/cuenta-usuario-routing.module';

@NgModule({
  imports: [SharedModule, CuentaUsuarioRoutingModule],
  declarations: [CuentaUsuarioComponent, CuentaUsuarioDetailComponent, CuentaUsuarioUpdateComponent, CuentaUsuarioDeleteDialogComponent],
  entryComponents: [CuentaUsuarioDeleteDialogComponent],
})
export class CuentaUsuarioModule {}
