import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAmigo } from '../amigo.model';
import { AmigoService } from '../service/amigo.service';

@Component({
  templateUrl: './amigo-delete-dialog.component.html',
})
export class AmigoDeleteDialogComponent {
  amigo?: IAmigo;

  constructor(protected amigoService: AmigoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.amigoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
