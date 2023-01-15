import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICompetidor } from '../competidor.model';
import { CompetidorService } from '../service/competidor.service';
import { CompetidorDeleteDialogComponent } from '../delete/competidor-delete-dialog.component';

@Component({
  selector: 'jhi-competidor',
  templateUrl: './competidor.component.html',
})
export class CompetidorComponent implements OnInit {
  competidors?: ICompetidor[];
  isLoading = false;

  constructor(protected competidorService: CompetidorService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.competidorService.query().subscribe({
      next: (res: HttpResponse<ICompetidor[]>) => {
        this.isLoading = false;
        this.competidors = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ICompetidor): number {
    return item.id!;
  }

  delete(competidor: ICompetidor): void {
    const modalRef = this.modalService.open(CompetidorDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.competidor = competidor;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
