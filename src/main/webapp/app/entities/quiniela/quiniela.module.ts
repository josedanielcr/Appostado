import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { QuinielaComponent } from './list/quiniela.component';
import { QuinielaDetailComponent } from './detail/quiniela-detail.component';
import { QuinielaUpdateComponent } from './update/quiniela-update.component';
import { QuinielaDeleteDialogComponent } from './delete/quiniela-delete-dialog.component';
import { QuinielaRoutingModule } from './route/quiniela-routing.module';

@NgModule({
  imports: [SharedModule, QuinielaRoutingModule],
  declarations: [QuinielaComponent, QuinielaDetailComponent, QuinielaUpdateComponent, QuinielaDeleteDialogComponent],
  entryComponents: [QuinielaDeleteDialogComponent],
})
export class QuinielaModule {}
