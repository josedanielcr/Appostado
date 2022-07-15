import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMisionTransaccion } from '../mision-transaccion.model';
import { MisionTransaccionService } from '../service/mision-transaccion.service';
import { MisionTransaccionDeleteDialogComponent } from '../delete/mision-transaccion-delete-dialog.component';

@Component({
  selector: 'jhi-mision-transaccion',
  templateUrl: './mision-transaccion.component.html',
})
export class MisionTransaccionComponent implements OnInit {
  misionTransaccions?: IMisionTransaccion[];
  isLoading = false;

  constructor(protected misionTransaccionService: MisionTransaccionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.misionTransaccionService.query().subscribe({
      next: (res: HttpResponse<IMisionTransaccion[]>) => {
        this.isLoading = false;
        this.misionTransaccions = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IMisionTransaccion): number {
    return item.id!;
  }

  delete(misionTransaccion: IMisionTransaccion): void {
    const modalRef = this.modalService.open(MisionTransaccionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.misionTransaccion = misionTransaccion;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
