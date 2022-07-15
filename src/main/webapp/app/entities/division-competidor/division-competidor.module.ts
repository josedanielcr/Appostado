import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DivisionCompetidorComponent } from './list/division-competidor.component';
import { DivisionCompetidorDetailComponent } from './detail/division-competidor-detail.component';
import { DivisionCompetidorUpdateComponent } from './update/division-competidor-update.component';
import { DivisionCompetidorDeleteDialogComponent } from './delete/division-competidor-delete-dialog.component';
import { DivisionCompetidorRoutingModule } from './route/division-competidor-routing.module';

@NgModule({
  imports: [SharedModule, DivisionCompetidorRoutingModule],
  declarations: [
    DivisionCompetidorComponent,
    DivisionCompetidorDetailComponent,
    DivisionCompetidorUpdateComponent,
    DivisionCompetidorDeleteDialogComponent,
  ],
  entryComponents: [DivisionCompetidorDeleteDialogComponent],
})
export class DivisionCompetidorModule {}
