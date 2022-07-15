import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICanje } from '../canje.model';
import { CanjeService } from '../service/canje.service';
import { CanjeDeleteDialogComponent } from '../delete/canje-delete-dialog.component';

@Component({
  selector: 'jhi-canje',
  templateUrl: './canje.component.html',
})
export class CanjeComponent implements OnInit {
  canjes?: ICanje[];
  isLoading = false;

  constructor(protected canjeService: CanjeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.canjeService.query().subscribe({
      next: (res: HttpResponse<ICanje[]>) => {
        this.isLoading = false;
        this.canjes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ICanje): number {
    return item.id!;
  }

  delete(canje: ICanje): void {
    const modalRef = this.modalService.open(CanjeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.canje = canje;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
