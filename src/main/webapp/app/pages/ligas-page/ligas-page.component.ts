import { Component, OnInit } from '@angular/core';
import { ILiga } from '../../entities/liga/liga.model';
import { HttpResponse } from '@angular/common/http';
import { LigaService } from '../../entities/liga/service/liga.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'jhi-ligas-page',
  templateUrl: './ligas-page.component.html',
  styleUrls: ['./ligas-page.component.scss', '../../../assets/styles1.css'],
})
export class LigasPageComponent implements OnInit {
  misLigas?: ILiga[];
  ligasAmigos?: ILiga[];
  loadingMisLigas = true;
  loadingLigasAmigos = true;

  constructor(protected ligaService: LigaService) {
    return;
  }

  loadMisLigas(): void {
    this.loadingMisLigas = true;
    this.ligaService.getMisLigas().subscribe({
      next: (res: HttpResponse<ILiga[]>) => {
        this.misLigas = res.body ?? [];
        this.loadingMisLigas = false;
        console.log(this.misLigas);
      },
      error: () => {
        this.loadingMisLigas = true;
      },
    });
  }

  loadLigasAmigos(): void {
    this.loadingLigasAmigos = true;

    this.ligaService.getLigasAmigos().subscribe({
      next: (res: HttpResponse<ILiga[]>) => {
        this.ligasAmigos = res.body ?? [];
        this.loadingLigasAmigos = false;
      },
      error: () => {
        this.loadingLigasAmigos = true;
      },
    });
  }

  ngOnInit(): void {
    this.loadMisLigas();
    this.loadLigasAmigos();
  }

  updateAll(): void {
    this.loadMisLigas();
    this.loadLigasAmigos();
  }

  abandonarLiga(id: number): void {
    Swal.fire({
      title: '¿Estás seguro de querer salir de esta liga de amigos?',
      showDenyButton: true,
      confirmButtonText: `Abandonar`,
      denyButtonText: `Cancelar`,
    }).then(result => {
      /* Read more about isConfirmed, isDenied below */
      if (result.isConfirmed) {
        this.delete(id);
      }
    });
  }

  delete(id: number): void {
    this.ligaService.abandonarLiga(id).subscribe({
      next: () => this.loadLigasAmigos(),
    });
  }
}
