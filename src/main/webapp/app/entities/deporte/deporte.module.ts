import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DeporteComponent } from './list/deporte.component';
import { DeporteDetailComponent } from './detail/deporte-detail.component';
import { DeporteUpdateComponent } from './update/deporte-update.component';
import { DeporteDeleteDialogComponent } from './delete/deporte-delete-dialog.component';
import { DeporteRoutingModule } from './route/deporte-routing.module';

@NgModule({
  imports: [SharedModule, DeporteRoutingModule],
  declarations: [DeporteComponent, DeporteDetailComponent, DeporteUpdateComponent, DeporteDeleteDialogComponent],
  entryComponents: [DeporteDeleteDialogComponent],
})
export class DeporteModule {}
