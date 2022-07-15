import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILiga } from '../liga.model';
import { LigaService } from '../service/liga.service';
import { LigaDeleteDialogComponent } from '../delete/liga-delete-dialog.component';

@Component({
  selector: 'jhi-liga',
  templateUrl: './liga.component.html',
})
export class LigaComponent implements OnInit {
  ligas?: ILiga[];
  isLoading = false;

  constructor(protected ligaService: LigaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.ligaService.query().subscribe({
      next: (res: HttpResponse<ILiga[]>) => {
        this.isLoading = false;
        this.ligas = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ILiga): number {
    return item.id!;
  }

  delete(liga: ILiga): void {
    const modalRef = this.modalService.open(LigaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.liga = liga;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
