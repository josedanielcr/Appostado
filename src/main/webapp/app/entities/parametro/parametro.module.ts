import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ParametroComponent } from './list/parametro.component';
import { ParametroDetailComponent } from './detail/parametro-detail.component';
import { ParametroUpdateComponent } from './update/parametro-update.component';
import { ParametroDeleteDialogComponent } from './delete/parametro-delete-dialog.component';
import { ParametroRoutingModule } from './route/parametro-routing.module';
import { ToolbarModule } from 'primeng/toolbar';
import { TableModule } from 'primeng/table';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';

@NgModule({
  imports: [SharedModule, ParametroRoutingModule, ToolbarModule, TableModule, InputTextModule, ButtonModule, RippleModule],
  declarations: [ParametroComponent, ParametroDetailComponent, ParametroUpdateComponent, ParametroDeleteDialogComponent],
  entryComponents: [ParametroDeleteDialogComponent],
  exports: [ParametroComponent, ParametroUpdateComponent],
})
export class ParametroModule {}
