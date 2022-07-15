import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICuentaUsuario } from '../cuenta-usuario.model';
import { CuentaUsuarioService } from '../service/cuenta-usuario.service';
import { CuentaUsuarioDeleteDialogComponent } from '../delete/cuenta-usuario-delete-dialog.component';

@Component({
  selector: 'jhi-cuenta-usuario',
  templateUrl: './cuenta-usuario.component.html',
})
export class CuentaUsuarioComponent implements OnInit {
  cuentaUsuarios?: ICuentaUsuario[];
  isLoading = false;

  constructor(protected cuentaUsuarioService: CuentaUsuarioService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.cuentaUsuarioService.query().subscribe({
      next: (res: HttpResponse<ICuentaUsuario[]>) => {
        this.isLoading = false;
        this.cuentaUsuarios = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ICuentaUsuario): number {
    return item.id!;
  }

  delete(cuentaUsuario: ICuentaUsuario): void {
    const modalRef = this.modalService.open(CuentaUsuarioDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cuentaUsuario = cuentaUsuario;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
