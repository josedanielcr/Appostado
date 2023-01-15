import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'score',
})
export class ScorePipe implements PipeTransform {
  transform(score: number | null | undefined): number {
    if (!score) {
      return 0;
    }
    return score;
  }
}
