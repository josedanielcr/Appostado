import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IApuestaTransaccion } from '../apuesta-transaccion.model';
import { ApuestaTransaccionService } from '../service/apuesta-transaccion.service';
import { ApuestaTransaccionDeleteDialogComponent } from '../delete/apuesta-transaccion-delete-dialog.component';

@Component({
  selector: 'jhi-apuesta-transaccion',
  templateUrl: './apuesta-transaccion.component.html',
})
export class ApuestaTransaccionComponent implements OnInit {
  apuestaTransaccions?: IApuestaTransaccion[];
  isLoading = false;

  constructor(protected apuestaTransaccionService: ApuestaTransaccionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.apuestaTransaccionService.query().subscribe({
      next: (res: HttpResponse<IApuestaTransaccion[]>) => {
        this.isLoading = false;
        this.apuestaTransaccions = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IApuestaTransaccion): number {
    return item.id!;
  }

  delete(apuestaTransaccion: IApuestaTransaccion): void {
    const modalRef = this.modalService.open(ApuestaTransaccionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.apuestaTransaccion = apuestaTransaccion;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
