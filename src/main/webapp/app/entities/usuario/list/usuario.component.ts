import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IUsuario } from '../usuario.model';
import { UsuarioService } from '../service/usuario.service';
import { UsuarioDeleteDialogComponent } from '../delete/usuario-delete-dialog.component';
import { AccountService } from '../../../core/auth/account.service';
import { Account } from '../../../core/auth/account.model';
import { UserManagementService } from '../../../admin/user-management/service/user-management.service';

@Component({
  selector: 'jhi-usuario',
  templateUrl: './usuario.component.html',
})
export class UsuarioComponent implements OnInit {
  currentAccount: Account | null = null;
  usuarios?: IUsuario[];
  isLoading = false;

  constructor(
    private userService: UserManagementService,
    protected usuarioService: UsuarioService,
    protected modalService: NgbModal,
    private accountService: AccountService
  ) {}

  loadAll(): void {
    this.isLoading = true;
    this.accountService.identity().subscribe(account => (this.currentAccount = account));
    this.usuarioService.query().subscribe({
      next: (res: HttpResponse<IUsuario[]>) => {
        this.isLoading = false;
        this.usuarios = res.body ?? [];
        console.log(this.usuarios);
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

  cambiarEstado(usuario: IUsuario): void {
    const modalRef = this.modalService.open(UsuarioDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.usuario = usuario;
    modalRef.closed.subscribe(reason => {
      if (reason === 'Desactivado') {
        this.loadAll();
      } else if (reason === 'Activado') {
        this.loadAll();
      }
    });
  }
}
