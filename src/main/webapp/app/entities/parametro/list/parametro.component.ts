import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IParametro } from '../parametro.model';
import { ParametroService } from '../service/parametro.service';
import { ParametroDeleteDialogComponent } from '../delete/parametro-delete-dialog.component';

@Component({
  selector: 'jhi-parametro',
  templateUrl: './parametro.component.html',
})
export class ParametroComponent implements OnInit {
  parametros?: IParametro[];
  isLoading = false;

  constructor(protected parametroService: ParametroService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.parametroService.query().subscribe({
      next: (res: HttpResponse<IParametro[]>) => {
        this.isLoading = false;
        this.parametros = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IParametro): number {
    return item.id!;
  }

  delete(parametro: IParametro): void {
    const modalRef = this.modalService.open(ParametroDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.parametro = parametro;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
