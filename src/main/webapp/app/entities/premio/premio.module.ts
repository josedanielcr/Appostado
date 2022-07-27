import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PremioComponent } from './list/premio.component';
import { PremioDetailComponent } from './detail/premio-detail.component';
import { PremioUpdateComponent } from './update/premio-update.component';
import { PremioDeleteDialogComponent } from './delete/premio-delete-dialog.component';
import { PremioRoutingModule } from './route/premio-routing.module';

@NgModule({
  imports: [SharedModule, PremioRoutingModule],
  declarations: [PremioComponent, PremioDetailComponent, PremioUpdateComponent, PremioDeleteDialogComponent],
  entryComponents: [PremioDeleteDialogComponent],
  exports: [PremioComponent],
})
export class PremioModule {}
