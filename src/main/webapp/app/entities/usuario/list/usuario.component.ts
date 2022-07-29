import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUsuario } from '../usuario.model';
import { UsuarioService } from '../service/usuario.service';
import { UsuarioDeleteDialogComponent } from '../delete/usuario-delete-dialog.component';
import Swal from 'sweetalert2';

@Component({
  selector: 'jhi-usuario',
  templateUrl: './usuario.component.html',
})
export class UsuarioComponent implements OnInit {
  usuarios?: IUsuario[];
  isLoading = false;

  constructor(protected usuarioService: UsuarioService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.usuarioService.query().subscribe({
      next: (res: HttpResponse<IUsuario[]>) => {
        this.isLoading = false;
        this.usuarios = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IUsuario): number {
    return item.id!;
  }

  inactivar(usuario: IUsuario): void {
    Swal.fire({
      icon: 'question',
      title: 'Estás seguro de que desea inactivar esta cuenta?',
      showDenyButton: true,
      confirmButtonColor: '#38b000',
      confirmButtonText: `Si`,
      denyButtonText: `No`,
    }).then(result => {
      if (result.isConfirmed) {
        this.usuarioService.inactivar(usuario.id!);
        Swal.fire({
          icon: 'success',
          title: 'Usuario inactivado',
          confirmButtonColor: '#38b000',
        });
      } else if (result.isDenied) {
        Swal.fire({
          icon: 'error',
          title: 'Acción cancelada',
          confirmButtonColor: '#38b000',
          timer: 10000,
        });
      }
    });
  }

  activar(usuario: IUsuario): void {
    const modalRef = this.modalService.open(UsuarioDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.usuario = usuario;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
