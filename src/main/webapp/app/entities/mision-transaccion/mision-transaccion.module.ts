import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MisionTransaccionComponent } from './list/mision-transaccion.component';
import { MisionTransaccionDetailComponent } from './detail/mision-transaccion-detail.component';
import { MisionTransaccionUpdateComponent } from './update/mision-transaccion-update.component';
import { MisionTransaccionDeleteDialogComponent } from './delete/mision-transaccion-delete-dialog.component';
import { MisionTransaccionRoutingModule } from './route/mision-transaccion-routing.module';

@NgModule({
  imports: [SharedModule, MisionTransaccionRoutingModule],
  declarations: [
    MisionTransaccionComponent,
    MisionTransaccionDetailComponent,
    MisionTransaccionUpdateComponent,
    MisionTransaccionDeleteDialogComponent,
  ],
  entryComponents: [MisionTransaccionDeleteDialogComponent],
})
export class MisionTransaccionModule {}
