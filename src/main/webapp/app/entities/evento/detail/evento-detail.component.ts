import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IEvento } from '../evento.model';
import { EventoDeleteDialogComponent } from '../cancel/evento-delete-dialog.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-evento-detail',
  templateUrl: './evento-detail.component.html',
})
export class EventoDetailComponent implements OnInit {
  evento: IEvento | null = null;
  isPendiente = true;
  isEnProgreso = true;

  constructor(protected activatedRoute: ActivatedRoute, protected modalService: NgbModal) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evento }) => {
      this.evento = evento;
      if (this.evento?.estado === 'Pendiente') {
        this.isPendiente = false;
      } else if (this.evento?.estado === 'En progreso') {
        this.isEnProgreso = false;
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  delete(evento: IEvento): void {
    const modalRef = this.modalService.open(EventoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.evento = evento;
  }
}
