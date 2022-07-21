import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EventoComponent } from './list/evento.component';
import { EventoDetailComponent } from './detail/evento-detail.component';
import { EventoUpdateComponent } from './create/evento-update.component';
import { EventoCancelDialogComponent } from './cancel/evento-cancel-dialog.component';
import { EventoRoutingModule } from './route/evento-routing.module';
import { ResolverComponent } from './resolver/resolver.component';
import { EventoDeleteDialogComponent } from './delete/evento-delete-dialog.component';

@NgModule({
  imports: [SharedModule, EventoRoutingModule],
  declarations: [
    EventoComponent,
    EventoDetailComponent,
    EventoUpdateComponent,
    EventoCancelDialogComponent,
    ResolverComponent,
    EventoDeleteDialogComponent,
  ],
  entryComponents: [EventoCancelDialogComponent],
  exports: [EventoComponent],
})
export class EventoModule {}
