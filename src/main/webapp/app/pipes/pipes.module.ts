import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ScorePipe } from './score.pipe';

@NgModule({
  declarations: [ScorePipe],
  exports: [ScorePipe],
  imports: [CommonModule],
})
export class PipesModule {}
