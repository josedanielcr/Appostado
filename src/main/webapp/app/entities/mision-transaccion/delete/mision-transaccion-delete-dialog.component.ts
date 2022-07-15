import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMisionTransaccion } from '../mision-transaccion.model';
import { MisionTransaccionService } from '../service/mision-transaccion.service';

@Component({
  templateUrl: './mision-transaccion-delete-dialog.component.html',
})
export class MisionTransaccionDeleteDialogComponent {
  misionTransaccion?: IMisionTransaccion;

  constructor(protected misionTransaccionService: MisionTransaccionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.misionTransaccionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
