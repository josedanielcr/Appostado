import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IPremio } from '../../entities/premio/premio.model';
import { ICanje } from '../../entities/canje/canje.model';
import { PremioService } from '../../entities/premio/service/premio.service';
import { CanjeService } from '../../entities/canje/service/canje.service';
import { FormGroup, FormControl } from '@angular/forms';
import Swal from 'sweetalert2';

@Component({
  selector: 'jhi-premios-page',
  templateUrl: './premios-page.component.html',
  styleUrls: ['./premios-page.component.scss'],
})
export class PremiosPageComponent implements OnInit {
  premios?: IPremio[];
  canje1?: ICanje;
  isLoading = false;
  premiosActivos?: IPremio[];
  public acomodos: any = [
    { orden: 'menor a mayor costo de créditos', valor: 1 },
    { orden: 'mayor a menor costo de créditos', valor: 2 },
    { orden: 'mayor a menor popularidad', valor: 3 },
    { orden: 'menor a mayor popularidad ', valor: 4 },
  ];
  filtrosForm = new FormGroup({
    filtro: new FormControl(''),
  });

  constructor(protected premioService: PremioService, protected modalService: NgbModal, protected canjeService: CanjeService) {
    return;
  }

  loadAll(): void {
    this.isLoading = true;

    this.premioService.findActivos().subscribe({
      next: (res: HttpResponse<IPremio[]>) => {
        this.isLoading = false;

        this.premios = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  loadOrder(acomodo: number): void {
    this.isLoading = true;

    this.premioService.findActivosFiltro(acomodo).subscribe({
      next: (res: HttpResponse<IPremio[]>) => {
        this.isLoading = false;

        this.premios = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IPremio): number {
    return item.id!;
  }

  onSubmit(): void {
    const filtro = this.filtrosForm.get(['filtro'])!.value;
    this.loadOrder(filtro);
  }

  canje(idPremio: any): void {
    console.log('hola');
  }
}
