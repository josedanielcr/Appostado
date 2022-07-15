import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeporte } from '../deporte.model';
import { DeporteService } from '../service/deporte.service';

@Component({
  templateUrl: './deporte-delete-dialog.component.html',
})
export class DeporteDeleteDialogComponent {
  deporte?: IDeporte;

  constructor(protected deporteService: DeporteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deporteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
