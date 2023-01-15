import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILigaUsuario } from '../liga-usuario.model';
import { LigaUsuarioService } from '../service/liga-usuario.service';

@Component({
  templateUrl: './liga-usuario-delete-dialog.component.html',
})
export class LigaUsuarioDeleteDialogComponent {
  ligaUsuario?: ILigaUsuario;

  constructor(protected ligaUsuarioService: LigaUsuarioService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ligaUsuarioService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
