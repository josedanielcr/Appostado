import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAmigo } from '../amigo.model';
import { AmigoService } from '../service/amigo.service';
import { AmigoDeleteDialogComponent } from '../delete/amigo-delete-dialog.component';

@Component({
  selector: 'jhi-amigo',
  templateUrl: './amigo.component.html',
})
export class AmigoComponent implements OnInit {
  amigos?: IAmigo[];
  isLoading = false;

  constructor(protected amigoService: AmigoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.amigoService.query().subscribe({
      next: (res: HttpResponse<IAmigo[]>) => {
        this.isLoading = false;
        this.amigos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IAmigo): number {
    return item.id!;
  }

  delete(amigo: IAmigo): void {
    const modalRef = this.modalService.open(AmigoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.amigo = amigo;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
