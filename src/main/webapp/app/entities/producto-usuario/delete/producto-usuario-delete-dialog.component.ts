import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductoUsuario } from '../producto-usuario.model';
import { ProductoUsuarioService } from '../service/producto-usuario.service';

@Component({
  templateUrl: './producto-usuario-delete-dialog.component.html',
})
export class ProductoUsuarioDeleteDialogComponent {
  productoUsuario?: IProductoUsuario;

  constructor(protected productoUsuarioService: ProductoUsuarioService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.productoUsuarioService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
