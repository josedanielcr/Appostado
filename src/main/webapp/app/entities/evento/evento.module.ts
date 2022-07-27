import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EventoComponent } from './list/evento.component';
import { EventoDetailComponent } from './detail/evento-detail.component';
import { EventoUpdateComponent } from './create/evento-update.component';
import { EventoDeleteDialogComponent } from './cancel/evento-delete-dialog.component';
import { EventoRoutingModule } from './route/evento-routing.module';
import { ResolverComponent } from './resolver/resolver.component';
import { ToolbarModule } from 'primeng/toolbar';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { InputTextModule } from 'primeng/inputtext';

@NgModule({
  imports: [SharedModule, EventoRoutingModule, ToolbarModule, TableModule, ButtonModule, RippleModule, InputTextModule],
  declarations: [EventoComponent, EventoDetailComponent, EventoUpdateComponent, EventoDeleteDialogComponent, ResolverComponent],
  entryComponents: [EventoDeleteDialogComponent],
  exports: [EventoComponent],
})
export class EventoModule {}
