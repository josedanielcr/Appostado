import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IQuiniela } from '../quiniela.model';
import { QuinielaService } from '../service/quiniela.service';
import { QuinielaDeleteDialogComponent } from '../delete/quiniela-delete-dialog.component';

@Component({
  selector: 'jhi-quiniela',
  templateUrl: './quiniela.component.html',
})
export class QuinielaComponent implements OnInit {
  quinielas?: IQuiniela[];
  isLoading = false;

  constructor(protected quinielaService: QuinielaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.quinielaService.query().subscribe({
      next: (res: HttpResponse<IQuiniela[]>) => {
        this.isLoading = false;
        this.quinielas = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IQuiniela): number {
    return item.id!;
  }

  delete(quiniela: IQuiniela): void {
    const modalRef = this.modalService.open(QuinielaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.quiniela = quiniela;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
