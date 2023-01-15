import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILigaUsuario } from '../liga-usuario.model';
import { LigaUsuarioService } from '../service/liga-usuario.service';
import { LigaUsuarioDeleteDialogComponent } from '../delete/liga-usuario-delete-dialog.component';

@Component({
  selector: 'jhi-liga-usuario',
  templateUrl: './liga-usuario.component.html',
})
export class LigaUsuarioComponent implements OnInit {
  ligaUsuarios?: ILigaUsuario[];
  isLoading = false;

  constructor(protected ligaUsuarioService: LigaUsuarioService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.ligaUsuarioService.query().subscribe({
      next: (res: HttpResponse<ILigaUsuario[]>) => {
        this.isLoading = false;
        this.ligaUsuarios = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ILigaUsuario): number {
    return item.id!;
  }

  delete(ligaUsuario: ILigaUsuario): void {
    const modalRef = this.modalService.open(LigaUsuarioDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ligaUsuario = ligaUsuario;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
