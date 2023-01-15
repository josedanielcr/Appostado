import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOpcionRol } from '../opcion-rol.model';
import { OpcionRolService } from '../service/opcion-rol.service';
import { OpcionRolDeleteDialogComponent } from '../delete/opcion-rol-delete-dialog.component';

@Component({
  selector: 'jhi-opcion-rol',
  templateUrl: './opcion-rol.component.html',
})
export class OpcionRolComponent implements OnInit {
  opcionRols?: IOpcionRol[];
  isLoading = false;

  constructor(protected opcionRolService: OpcionRolService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.opcionRolService.query().subscribe({
      next: (res: HttpResponse<IOpcionRol[]>) => {
        this.isLoading = false;
        this.opcionRols = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IOpcionRol): number {
    return item.id!;
  }

  delete(opcionRol: IOpcionRol): void {
    const modalRef = this.modalService.open(OpcionRolDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.opcionRol = opcionRol;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
