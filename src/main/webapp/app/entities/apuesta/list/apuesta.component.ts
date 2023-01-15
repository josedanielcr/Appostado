import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IApuesta } from '../apuesta.model';
import { ApuestaService } from '../service/apuesta.service';
import { ApuestaDeleteDialogComponent } from '../delete/apuesta-delete-dialog.component';

@Component({
  selector: 'jhi-apuesta',
  templateUrl: './apuesta.component.html',
})
export class ApuestaComponent implements OnInit {
  apuestas?: IApuesta[];
  isLoading = false;

  constructor(protected apuestaService: ApuestaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.apuestaService.query().subscribe({
      next: (res: HttpResponse<IApuesta[]>) => {
        this.isLoading = false;
        this.apuestas = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IApuesta): number {
    return item.id!;
  }

  delete(apuesta: IApuesta): void {
    const modalRef = this.modalService.open(ApuestaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.apuesta = apuesta;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
