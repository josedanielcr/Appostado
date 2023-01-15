import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMision } from '../mision.model';
import { MisionService } from '../service/mision.service';

@Component({
  templateUrl: './mision-delete-dialog.component.html',
})
export class MisionDeleteDialogComponent {
  mision?: IMision;

  constructor(protected misionService: MisionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.misionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
