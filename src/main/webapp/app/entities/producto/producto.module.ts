import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProductoComponent } from './list/producto.component';
import { ProductoDetailComponent } from './detail/producto-detail.component';
import { ProductoUpdateComponent } from './update/producto-update.component';
import { ProductoDeleteDialogComponent } from './delete/producto-delete-dialog.component';
import { ProductoRoutingModule } from './route/producto-routing.module';
import { ToolbarModule } from 'primeng/toolbar';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { TableModule } from 'primeng/table';

@NgModule({
  imports: [SharedModule, ProductoRoutingModule, ToolbarModule, ButtonModule, RippleModule, TableModule],
  declarations: [ProductoComponent, ProductoDetailComponent, ProductoUpdateComponent, ProductoDeleteDialogComponent],
  entryComponents: [ProductoDeleteDialogComponent],
})
export class ProductoModule {}
