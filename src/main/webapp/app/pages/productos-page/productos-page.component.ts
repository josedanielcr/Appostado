import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProducto } from '../../entities/producto/producto.model';
import { ProductoService } from '../../entities/producto/service/producto.service';
import { FormGroup, FormControl } from '@angular/forms';
import Swal from 'sweetalert2';

@Component({
  selector: 'jhi-productos-page',
  templateUrl: './productos-page.component.html',
  styleUrls: ['./productos-page.component.css'],
})
export class ProductosPageComponent implements OnInit {
  productosCodigo?: IProducto[];
  productos?: IProducto[];
  isLoading = false;
  respuesta = '';
  public acomodos: any = [
    { orden: 'menor a mayor descuento/créditos', valor: 1 },
    { orden: 'mayor a menor descuento/créditos', valor: 2 },
    { orden: 'mayor a menor popularidad', valor: 3 },
    { orden: 'menor a mayor popularidad ', valor: 4 },
  ];
  filtrosForm = new FormGroup({
    filtro: new FormControl(''),
  });
  constructor(protected productoService: ProductoService, protected modalService: NgbModal) {
    return;
  }

  loadAllCodifoFijo(): void {
    this.isLoading = true;

    this.productoService.findAllCodigoFijo().subscribe({
      next: (res: HttpResponse<IProducto[]>) => {
        this.isLoading = false;
        this.productosCodigo = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  loadAllSinCodigo(): void {
    this.isLoading = true;

    this.productoService.findAllSinCodigo().subscribe({
      next: (res: HttpResponse<IProducto[]>) => {
        this.isLoading = false;
        this.productos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  loadAllCodifoFijoOrder(orden: number): void {
    this.isLoading = true;

    this.productoService.findAllCodigoFijoOrder(orden).subscribe({
      next: (res: HttpResponse<IProducto[]>) => {
        this.isLoading = false;
        this.productosCodigo = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  loadAllSinCodigoOrder(orden: number): void {
    this.isLoading = true;

    this.productoService.findAllSinCodigoOrder(orden).subscribe({
      next: (res: HttpResponse<IProducto[]>) => {
        this.isLoading = false;
        this.productos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAllCodifoFijo();
    this.loadAllSinCodigo();
  }

  trackId(_index: number, item: IProducto): number {
    return item.id!;
  }

  onSubmit(): void {
    const filtro = this.filtrosForm.get(['filtro'])!.value;
    this.loadAllCodifoFijoOrder(filtro);
    this.loadAllSinCodigoOrder(filtro);
  }

  bonificar(idProducto: any): void {
    this.productoService.bonificacion(idProducto).subscribe(
      data => {
        this.respuesta = data;
        if (this.respuesta === 'si') {
          Swal.fire({
            icon: 'success',
            title: 'Bonificación en progreso',
            text: 'Enviaremos los detalles para el canje por medio de correo.',
            confirmButtonColor: '#38b000',
          });
        } else {
          Swal.fire({
            icon: 'error',
            title: 'Oopss...',
            text: 'Ha ocurrido un error.',
            confirmButtonColor: '#38b000',
            timer: 10000,
          });
        }
      },
      error => {
        Swal.fire({
          icon: 'error',
          title: 'Oopss...',
          text: error,
          confirmButtonColor: '#38b000',
          timer: 10000,
        });
      }
    );
  }
}
