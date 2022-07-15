import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProductoUsuario } from '../producto-usuario.model';
import { ProductoUsuarioService } from '../service/producto-usuario.service';
import { ProductoUsuarioDeleteDialogComponent } from '../delete/producto-usuario-delete-dialog.component';

@Component({
  selector: 'jhi-producto-usuario',
  templateUrl: './producto-usuario.component.html',
})
export class ProductoUsuarioComponent implements OnInit {
  productoUsuarios?: IProductoUsuario[];
  isLoading = false;

  constructor(protected productoUsuarioService: ProductoUsuarioService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.productoUsuarioService.query().subscribe({
      next: (res: HttpResponse<IProductoUsuario[]>) => {
        this.isLoading = false;
        this.productoUsuarios = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IProductoUsuario): number {
    return item.id!;
  }

  delete(productoUsuario: IProductoUsuario): void {
    const modalRef = this.modalService.open(ProductoUsuarioDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.productoUsuario = productoUsuario;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
