import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IApuesta } from '../apuesta.model';
import { ApuestaService } from '../service/apuesta.service';

@Component({
  templateUrl: './apuesta-delete-dialog.component.html',
})
export class ApuestaDeleteDialogComponent {
  apuesta?: IApuesta;

  constructor(protected apuestaService: ApuestaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.apuestaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
