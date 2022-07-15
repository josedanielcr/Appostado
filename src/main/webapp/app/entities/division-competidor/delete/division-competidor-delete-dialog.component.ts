import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDivisionCompetidor } from '../division-competidor.model';
import { DivisionCompetidorService } from '../service/division-competidor.service';

@Component({
  templateUrl: './division-competidor-delete-dialog.component.html',
})
export class DivisionCompetidorDeleteDialogComponent {
  divisionCompetidor?: IDivisionCompetidor;

  constructor(protected divisionCompetidorService: DivisionCompetidorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.divisionCompetidorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
