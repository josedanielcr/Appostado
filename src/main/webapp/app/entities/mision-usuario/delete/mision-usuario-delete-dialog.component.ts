import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMisionUsuario } from '../mision-usuario.model';
import { MisionUsuarioService } from '../service/mision-usuario.service';

@Component({
  templateUrl: './mision-usuario-delete-dialog.component.html',
})
export class MisionUsuarioDeleteDialogComponent {
  misionUsuario?: IMisionUsuario;

  constructor(protected misionUsuarioService: MisionUsuarioService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.misionUsuarioService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
