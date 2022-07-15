import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IApuestaTransaccion } from '../apuesta-transaccion.model';
import { ApuestaTransaccionService } from '../service/apuesta-transaccion.service';

@Component({
  templateUrl: './apuesta-transaccion-delete-dialog.component.html',
})
export class ApuestaTransaccionDeleteDialogComponent {
  apuestaTransaccion?: IApuestaTransaccion;

  constructor(protected apuestaTransaccionService: ApuestaTransaccionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.apuestaTransaccionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
