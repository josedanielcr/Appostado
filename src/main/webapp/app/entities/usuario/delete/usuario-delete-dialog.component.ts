import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUsuario } from '../usuario.model';
import { UsuarioService } from '../service/usuario.service';

@Component({
  templateUrl: './usuario-delete-dialog.component.html',
})
export class UsuarioDeleteDialogComponent {
  usuario?: IUsuario;

  constructor(protected usuarioService: UsuarioService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  cambiarEstado(id: number): void {
    if (this.usuario?.user?.activated === true) {
      this.usuarioService.inactivar(id).subscribe(() => {
        this.activeModal.close('Desactivado');
      });
    } else {
      this.usuarioService.activar(id).subscribe(() => {
        this.activeModal.close('Activado');
      });
    }
  }
}
