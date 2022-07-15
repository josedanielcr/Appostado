import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ApuestaComponent } from './list/apuesta.component';
import { ApuestaDetailComponent } from './detail/apuesta-detail.component';
import { ApuestaUpdateComponent } from './update/apuesta-update.component';
import { ApuestaDeleteDialogComponent } from './delete/apuesta-delete-dialog.component';
import { ApuestaRoutingModule } from './route/apuesta-routing.module';

@NgModule({
  imports: [SharedModule, ApuestaRoutingModule],
  declarations: [ApuestaComponent, ApuestaDetailComponent, ApuestaUpdateComponent, ApuestaDeleteDialogComponent],
  entryComponents: [ApuestaDeleteDialogComponent],
})
export class ApuestaModule {}
