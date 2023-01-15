import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MisionComponent } from './list/mision.component';
import { MisionDetailComponent } from './detail/mision-detail.component';
import { MisionUpdateComponent } from './update/mision-update.component';
import { MisionDeleteDialogComponent } from './delete/mision-delete-dialog.component';
import { MisionRoutingModule } from './route/mision-routing.module';
import { ToolbarModule } from 'primeng/toolbar';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { TableModule } from 'primeng/table';

@NgModule({
  imports: [SharedModule, MisionRoutingModule, ToolbarModule, ButtonModule, RippleModule, TableModule],
  declarations: [MisionComponent, MisionDetailComponent, MisionUpdateComponent, MisionDeleteDialogComponent],
  entryComponents: [MisionDeleteDialogComponent],
})
export class MisionModule {}
