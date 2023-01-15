import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LigaUsuarioComponent } from './list/liga-usuario.component';
import { LigaUsuarioDetailComponent } from './detail/liga-usuario-detail.component';
import { LigaUsuarioUpdateComponent } from './update/liga-usuario-update.component';
import { LigaUsuarioDeleteDialogComponent } from './delete/liga-usuario-delete-dialog.component';
import { LigaUsuarioRoutingModule } from './route/liga-usuario-routing.module';

@NgModule({
  imports: [SharedModule, LigaUsuarioRoutingModule],
  declarations: [LigaUsuarioComponent, LigaUsuarioDetailComponent, LigaUsuarioUpdateComponent, LigaUsuarioDeleteDialogComponent],
  entryComponents: [LigaUsuarioDeleteDialogComponent],
})
export class LigaUsuarioModule {}
