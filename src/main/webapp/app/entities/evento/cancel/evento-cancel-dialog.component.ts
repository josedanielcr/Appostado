import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEvento } from '../evento.model';
import { EventoService } from '../service/evento.service';

@Component({
  templateUrl: './evento-cancel-dialog.component.html',
})
export class EventoCancelDialogComponent {
  evento?: IEvento;

  constructor(protected eventoService: EventoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.eventoService.cancelar(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
    this.previousState();
  }

  previousState(): void {
    window.history.back();
  }
}
