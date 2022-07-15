import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LigaComponent } from './list/liga.component';
import { LigaDetailComponent } from './detail/liga-detail.component';
import { LigaUpdateComponent } from './update/liga-update.component';
import { LigaDeleteDialogComponent } from './delete/liga-delete-dialog.component';
import { LigaRoutingModule } from './route/liga-routing.module';

@NgModule({
  imports: [SharedModule, LigaRoutingModule],
  declarations: [LigaComponent, LigaDetailComponent, LigaUpdateComponent, LigaDeleteDialogComponent],
  entryComponents: [LigaDeleteDialogComponent],
})
export class LigaModule {}
