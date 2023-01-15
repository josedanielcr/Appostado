import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MisionUsuarioComponent } from './list/mision-usuario.component';
import { MisionUsuarioDetailComponent } from './detail/mision-usuario-detail.component';
import { MisionUsuarioUpdateComponent } from './update/mision-usuario-update.component';
import { MisionUsuarioDeleteDialogComponent } from './delete/mision-usuario-delete-dialog.component';
import { MisionUsuarioRoutingModule } from './route/mision-usuario-routing.module';

@NgModule({
  imports: [SharedModule, MisionUsuarioRoutingModule],
  declarations: [MisionUsuarioComponent, MisionUsuarioDetailComponent, MisionUsuarioUpdateComponent, MisionUsuarioDeleteDialogComponent],
  entryComponents: [MisionUsuarioDeleteDialogComponent],
})
export class MisionUsuarioModule {}
