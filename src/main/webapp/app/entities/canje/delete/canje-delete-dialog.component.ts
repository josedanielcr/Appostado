import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICanje } from '../canje.model';
import { CanjeService } from '../service/canje.service';

@Component({
  templateUrl: './canje-delete-dialog.component.html',
})
export class CanjeDeleteDialogComponent {
  canje?: ICanje;

  constructor(protected canjeService: CanjeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.canjeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
