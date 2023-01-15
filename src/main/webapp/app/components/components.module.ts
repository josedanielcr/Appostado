import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EventCardComponent } from './event-card/event-card.component';
import { PipesModule } from '../pipes/pipes.module';
import { RouterModule } from '@angular/router';
import { EventParticipantsComponent } from './event-participants/event-participants.component';
import { EventInfoComponent } from './event-info/event-info.component';
import { BetEventComponent } from './bet-event/bet-event.component';
import { ReactiveFormsModule } from '@angular/forms';
import { SliderModule } from 'primeng/slider';
import { CheckboxModule } from 'primeng/checkbox';
import { NotificationComponent } from './notification/notification.component';
import { NotificationGridComponent } from './notification-grid/notification-grid.component';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  declarations: [
    EventCardComponent,
    EventParticipantsComponent,
    EventInfoComponent,
    BetEventComponent,
    NotificationComponent,
    NotificationGridComponent,
  ],
  imports: [CommonModule, PipesModule, RouterModule, ReactiveFormsModule, SliderModule, CheckboxModule, SharedModule],
  exports: [
    EventCardComponent,
    EventParticipantsComponent,
    EventInfoComponent,
    BetEventComponent,
    NotificationComponent,
    NotificationGridComponent,
  ],
})
export class ComponentsModule {}
