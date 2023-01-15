import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMisionUsuario } from '../mision-usuario.model';
import { MisionUsuarioService } from '../service/mision-usuario.service';
import { MisionUsuarioDeleteDialogComponent } from '../delete/mision-usuario-delete-dialog.component';

@Component({
  selector: 'jhi-mision-usuario',
  templateUrl: './mision-usuario.component.html',
})
export class MisionUsuarioComponent implements OnInit {
  misionUsuarios?: IMisionUsuario[];
  isLoading = false;

  constructor(protected misionUsuarioService: MisionUsuarioService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.misionUsuarioService.query().subscribe({
      next: (res: HttpResponse<IMisionUsuario[]>) => {
        this.isLoading = false;
        this.misionUsuarios = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IMisionUsuario): number {
    return item.id!;
  }

  delete(misionUsuario: IMisionUsuario): void {
    const modalRef = this.modalService.open(MisionUsuarioDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.misionUsuario = misionUsuario;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
