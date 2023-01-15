import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPremio } from '../premio.model';
import { PremioService } from '../service/premio.service';
import { CanjeService } from '../../canje/service/canje.service';
import { ICanje } from '../../canje/canje.model';
import { PremioDeleteDialogComponent } from '../delete/premio-delete-dialog.component';

@Component({
  selector: 'jhi-premio',
  templateUrl: './premio.component.html',
})
export class PremioComponent implements OnInit {
  premios?: IPremio[];
  isLoading = false;
  canjes?: ICanje[];

  constructor(protected premioService: PremioService, protected modalService: NgbModal, protected canjeService: CanjeService) {}

  loadAll(): void {
    this.isLoading = true;

    this.premioService.query().subscribe({
      next: (res: HttpResponse<IPremio[]>) => {
        this.isLoading = false;
        this.premios = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  loadAllCanjesPendientes(): void {
    this.isLoading = true;

    this.canjeService.getPendientes().subscribe({
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
    this.loadAllCanjesPendientes();
  }

  trackId(_index: number, item: IPremio): number {
    return item.id!;
  }

  trackIdCanje(_index: number, item: ICanje): number {
    return item.id!;
  }

  delete(premio: IPremio): void {
    const modalRef = this.modalService.open(PremioDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.premio = premio;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
