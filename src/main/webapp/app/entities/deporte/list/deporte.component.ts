import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeporte } from '../deporte.model';
import { DeporteService } from '../service/deporte.service';
import { DeporteDeleteDialogComponent } from '../delete/deporte-delete-dialog.component';

@Component({
  selector: 'jhi-deporte',
  templateUrl: './deporte.component.html',
})
export class DeporteComponent implements OnInit {
  deportes?: IDeporte[];
  isLoading = false;

  constructor(protected deporteService: DeporteService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.deporteService.query().subscribe({
      next: (res: HttpResponse<IDeporte[]>) => {
        this.isLoading = false;
        this.deportes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IDeporte): number {
    return item.id!;
  }

  delete(deporte: IDeporte): void {
    const modalRef = this.modalService.open(DeporteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.deporte = deporte;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
