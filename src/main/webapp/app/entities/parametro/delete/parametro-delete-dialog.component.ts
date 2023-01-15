import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IParametro } from '../parametro.model';
import { ParametroService } from '../service/parametro.service';

@Component({
  templateUrl: './parametro-delete-dialog.component.html',
})
export class ParametroDeleteDialogComponent {
  parametro?: IParametro;

  constructor(protected parametroService: ParametroService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.parametroService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
