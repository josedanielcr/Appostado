import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICuentaUsuario } from '../cuenta-usuario.model';
import { CuentaUsuarioService } from '../service/cuenta-usuario.service';

@Component({
  templateUrl: './cuenta-usuario-delete-dialog.component.html',
})
export class CuentaUsuarioDeleteDialogComponent {
  cuentaUsuario?: ICuentaUsuario;

  constructor(protected cuentaUsuarioService: CuentaUsuarioService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cuentaUsuarioService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
