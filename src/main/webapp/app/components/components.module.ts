import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EventCardComponent } from './event-card/event-card.component';
import { PipesModule } from '../pipes/pipes.module';

@NgModule({
  declarations: [EventCardComponent],
  imports: [CommonModule, PipesModule],
  exports: [EventCardComponent],
})
export class ComponentsModule {}
