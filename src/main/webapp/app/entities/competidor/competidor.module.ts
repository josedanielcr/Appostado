import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CompetidorComponent } from './list/competidor.component';
import { CompetidorDetailComponent } from './detail/competidor-detail.component';
import { CompetidorUpdateComponent } from './update/competidor-update.component';
import { CompetidorDeleteDialogComponent } from './delete/competidor-delete-dialog.component';
import { CompetidorRoutingModule } from './route/competidor-routing.module';
import { FileUploadModule } from 'ng2-file-upload';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ToolbarModule } from 'primeng/toolbar';

@NgModule({
  imports: [SharedModule, CompetidorRoutingModule, FileUploadModule, TableModule, ButtonModule, ToolbarModule],
  declarations: [CompetidorComponent, CompetidorDetailComponent, CompetidorUpdateComponent, CompetidorDeleteDialogComponent],
  entryComponents: [CompetidorDeleteDialogComponent],
  exports: [CompetidorComponent],
})
export class CompetidorModule {}
