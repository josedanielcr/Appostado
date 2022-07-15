import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDivisionCompetidor } from '../division-competidor.model';
import { DivisionCompetidorService } from '../service/division-competidor.service';
import { DivisionCompetidorDeleteDialogComponent } from '../delete/division-competidor-delete-dialog.component';

@Component({
  selector: 'jhi-division-competidor',
  templateUrl: './division-competidor.component.html',
})
export class DivisionCompetidorComponent implements OnInit {
  divisionCompetidors?: IDivisionCompetidor[];
  isLoading = false;

  constructor(protected divisionCompetidorService: DivisionCompetidorService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.divisionCompetidorService.query().subscribe({
      next: (res: HttpResponse<IDivisionCompetidor[]>) => {
        this.isLoading = false;
        this.divisionCompetidors = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IDivisionCompetidor): number {
    return item.id!;
  }

  delete(divisionCompetidor: IDivisionCompetidor): void {
    const modalRef = this.modalService.open(DivisionCompetidorDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.divisionCompetidor = divisionCompetidor;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
