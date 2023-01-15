import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ApuestaTransaccionComponent } from './list/apuesta-transaccion.component';
import { ApuestaTransaccionDetailComponent } from './detail/apuesta-transaccion-detail.component';
import { ApuestaTransaccionUpdateComponent } from './update/apuesta-transaccion-update.component';
import { ApuestaTransaccionDeleteDialogComponent } from './delete/apuesta-transaccion-delete-dialog.component';
import { ApuestaTransaccionRoutingModule } from './route/apuesta-transaccion-routing.module';

@NgModule({
  imports: [SharedModule, ApuestaTransaccionRoutingModule],
  declarations: [
    ApuestaTransaccionComponent,
    ApuestaTransaccionDetailComponent,
    ApuestaTransaccionUpdateComponent,
    ApuestaTransaccionDeleteDialogComponent,
  ],
  entryComponents: [ApuestaTransaccionDeleteDialogComponent],
})
export class ApuestaTransaccionModule {}
