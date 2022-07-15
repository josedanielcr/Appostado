import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITransaccion } from '../transaccion.model';
import { TransaccionService } from '../service/transaccion.service';
import { TransaccionDeleteDialogComponent } from '../delete/transaccion-delete-dialog.component';

@Component({
  selector: 'jhi-transaccion',
  templateUrl: './transaccion.component.html',
})
export class TransaccionComponent implements OnInit {
  transaccions?: ITransaccion[];
  isLoading = false;

  constructor(protected transaccionService: TransaccionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.transaccionService.query().subscribe({
      next: (res: HttpResponse<ITransaccion[]>) => {
        this.isLoading = false;
        this.transaccions = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ITransaccion): number {
    return item.id!;
  }

  delete(transaccion: ITransaccion): void {
    const modalRef = this.modalService.open(TransaccionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.transaccion = transaccion;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
