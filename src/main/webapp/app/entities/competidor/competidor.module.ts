import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CompetidorComponent } from './list/competidor.component';
import { CompetidorDetailComponent } from './detail/competidor-detail.component';
import { CompetidorUpdateComponent } from './update/competidor-update.component';
import { CompetidorDeleteDialogComponent } from './delete/competidor-delete-dialog.component';
import { CompetidorRoutingModule } from './route/competidor-routing.module';

@NgModule({
  imports: [SharedModule, CompetidorRoutingModule],
  declarations: [CompetidorComponent, CompetidorDetailComponent, CompetidorUpdateComponent, CompetidorDeleteDialogComponent],
  entryComponents: [CompetidorDeleteDialogComponent],
})
export class CompetidorModule {}
