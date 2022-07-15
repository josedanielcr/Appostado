import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IQuiniela } from '../quiniela.model';
import { QuinielaService } from '../service/quiniela.service';

@Component({
  templateUrl: './quiniela-delete-dialog.component.html',
})
export class QuinielaDeleteDialogComponent {
  quiniela?: IQuiniela;

  constructor(protected quinielaService: QuinielaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.quinielaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
