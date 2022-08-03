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

@NgModule({
  declarations: [EventCardComponent, EventParticipantsComponent, EventInfoComponent, BetEventComponent],
  imports: [CommonModule, PipesModule, RouterModule, ReactiveFormsModule, SliderModule, CheckboxModule],
  exports: [EventCardComponent, EventParticipantsComponent, EventInfoComponent, BetEventComponent],
})
export class ComponentsModule {}
