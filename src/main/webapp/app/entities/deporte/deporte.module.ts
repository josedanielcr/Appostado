import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DeporteComponent } from './list/deporte.component';
import { DeporteDetailComponent } from './detail/deporte-detail.component';
import { DeporteUpdateComponent } from './update/deporte-update.component';
import { DeporteDeleteDialogComponent } from './delete/deporte-delete-dialog.component';
import { DeporteRoutingModule } from './route/deporte-routing.module';
import { ToolbarModule } from 'primeng/toolbar';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { InputTextModule } from 'primeng/inputtext';

@NgModule({
  imports: [SharedModule, DeporteRoutingModule, ToolbarModule, TableModule, ButtonModule, RippleModule, InputTextModule],
  declarations: [DeporteComponent, DeporteDetailComponent, DeporteUpdateComponent, DeporteDeleteDialogComponent],
  entryComponents: [DeporteDeleteDialogComponent],
  exports: [DeporteComponent],
})
export class DeporteModule {}
