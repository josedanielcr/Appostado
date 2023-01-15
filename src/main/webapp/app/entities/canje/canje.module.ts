import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CanjeComponent } from './list/canje.component';
import { CanjeDetailComponent } from './detail/canje-detail.component';
import { CanjeUpdateComponent } from './update/canje-update.component';
import { CanjeDeleteDialogComponent } from './delete/canje-delete-dialog.component';
import { CanjeRoutingModule } from './route/canje-routing.module';

@NgModule({
  imports: [SharedModule, CanjeRoutingModule],
  declarations: [CanjeComponent, CanjeDetailComponent, CanjeUpdateComponent, CanjeDeleteDialogComponent],
  entryComponents: [CanjeDeleteDialogComponent],
})
export class CanjeModule {}
