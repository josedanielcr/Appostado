import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProductoUsuarioComponent } from './list/producto-usuario.component';
import { ProductoUsuarioDetailComponent } from './detail/producto-usuario-detail.component';
import { ProductoUsuarioUpdateComponent } from './update/producto-usuario-update.component';
import { ProductoUsuarioDeleteDialogComponent } from './delete/producto-usuario-delete-dialog.component';
import { ProductoUsuarioRoutingModule } from './route/producto-usuario-routing.module';

@NgModule({
  imports: [SharedModule, ProductoUsuarioRoutingModule],
  declarations: [
    ProductoUsuarioComponent,
    ProductoUsuarioDetailComponent,
    ProductoUsuarioUpdateComponent,
    ProductoUsuarioDeleteDialogComponent,
  ],
  entryComponents: [ProductoUsuarioDeleteDialogComponent],
})
export class ProductoUsuarioModule {}
