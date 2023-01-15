import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OpcionRolComponent } from './list/opcion-rol.component';
import { OpcionRolDetailComponent } from './detail/opcion-rol-detail.component';
import { OpcionRolUpdateComponent } from './update/opcion-rol-update.component';
import { OpcionRolDeleteDialogComponent } from './delete/opcion-rol-delete-dialog.component';
import { OpcionRolRoutingModule } from './route/opcion-rol-routing.module';

@NgModule({
  imports: [SharedModule, OpcionRolRoutingModule],
  declarations: [OpcionRolComponent, OpcionRolDetailComponent, OpcionRolUpdateComponent, OpcionRolDeleteDialogComponent],
  entryComponents: [OpcionRolDeleteDialogComponent],
})
export class OpcionRolModule {}
