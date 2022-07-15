import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AmigoComponent } from './list/amigo.component';
import { AmigoDetailComponent } from './detail/amigo-detail.component';
import { AmigoUpdateComponent } from './update/amigo-update.component';
import { AmigoDeleteDialogComponent } from './delete/amigo-delete-dialog.component';
import { AmigoRoutingModule } from './route/amigo-routing.module';

@NgModule({
  imports: [SharedModule, AmigoRoutingModule],
  declarations: [AmigoComponent, AmigoDetailComponent, AmigoUpdateComponent, AmigoDeleteDialogComponent],
  entryComponents: [AmigoDeleteDialogComponent],
})
export class AmigoModule {}
