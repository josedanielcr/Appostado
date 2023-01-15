import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICompetidor } from '../competidor.model';
import { CompetidorService } from '../service/competidor.service';

@Component({
  templateUrl: './competidor-delete-dialog.component.html',
})
export class CompetidorDeleteDialogComponent {
  competidor?: ICompetidor;

  constructor(protected competidorService: CompetidorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.competidorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
