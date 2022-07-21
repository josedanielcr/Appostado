import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IEvento } from '../evento.model';
import { EventoCancelDialogComponent } from '../cancel/evento-cancel-dialog.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { EventoDeleteDialogComponent } from '../delete/evento-delete-dialog.component';

@Component({
  selector: 'jhi-evento-detail',
  templateUrl: './evento-detail.component.html',
})
export class EventoDetailComponent implements OnInit {
  evento: IEvento | null = null;
  isPendiente = true;
  isEnProgreso = true;
  isCancelado = true;

  constructor(protected activatedRoute: ActivatedRoute, protected modalService: NgbModal) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evento }) => {
      this.evento = evento;
      if (this.evento?.estado === 'Pendiente') {
        this.isPendiente = false;
      } else if (this.evento?.estado === 'En progreso') {
        this.isEnProgreso = false;
      } else if (this.evento?.estado === 'Cancelado') {
        this.isCancelado = false;
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  cancel(evento: IEvento): void {
    const modalRef = this.modalService.open(EventoCancelDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.evento = evento;
  }

  delete(evento: IEvento): void {
    const modalRef = this.modalService.open(EventoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.evento = evento;
  }
}
