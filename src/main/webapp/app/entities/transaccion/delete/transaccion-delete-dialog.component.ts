import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITransaccion } from '../transaccion.model';
import { TransaccionService } from '../service/transaccion.service';

@Component({
  templateUrl: './transaccion-delete-dialog.component.html',
})
export class TransaccionDeleteDialogComponent {
  transaccion?: ITransaccion;

  constructor(protected transaccionService: TransaccionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.transaccionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
