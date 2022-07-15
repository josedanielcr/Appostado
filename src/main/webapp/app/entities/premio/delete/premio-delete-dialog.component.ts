import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPremio } from '../premio.model';
import { PremioService } from '../service/premio.service';

@Component({
  templateUrl: './premio-delete-dialog.component.html',
})
export class PremioDeleteDialogComponent {
  premio?: IPremio;

  constructor(protected premioService: PremioService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.premioService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
