import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILiga } from '../liga.model';
import { LigaService } from '../service/liga.service';

@Component({
  templateUrl: './liga-delete-dialog.component.html',
})
export class LigaDeleteDialogComponent {
  liga?: ILiga;

  constructor(protected ligaService: LigaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ligaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
