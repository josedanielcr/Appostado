import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMision } from '../mision.model';
import { MisionService } from '../service/mision.service';
import { MisionDeleteDialogComponent } from '../delete/mision-delete-dialog.component';

@Component({
  selector: 'jhi-mision',
  templateUrl: './mision.component.html',
})
export class MisionComponent implements OnInit {
  misions?: IMision[];
  isLoading = false;

  constructor(protected misionService: MisionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.misionService.query().subscribe({
      next: (res: HttpResponse<IMision[]>) => {
        this.isLoading = false;
        this.misions = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IMision): number {
    return item.id!;
  }

  delete(mision: IMision): void {
    const modalRef = this.modalService.open(MisionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.mision = mision;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
