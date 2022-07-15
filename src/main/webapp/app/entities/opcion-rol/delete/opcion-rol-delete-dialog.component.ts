import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOpcionRol } from '../opcion-rol.model';
import { OpcionRolService } from '../service/opcion-rol.service';

@Component({
  templateUrl: './opcion-rol-delete-dialog.component.html',
})
export class OpcionRolDeleteDialogComponent {
  opcionRol?: IOpcionRol;

  constructor(protected opcionRolService: OpcionRolService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.opcionRolService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
