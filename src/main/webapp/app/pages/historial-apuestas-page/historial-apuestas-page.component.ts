import { Component, OnInit } from '@angular/core';
import { AmigoDetail } from '../amigos-page/amigos-page.model';
import { AccountService } from '../../core/auth/account.service';
import { HttpResponse } from '@angular/common/http';
import { Table } from 'primeng/table';
import { IApuesta } from '../../entities/apuesta/apuesta.model';
import { ApuestaService } from '../../entities/apuesta/service/apuesta.service';
import { HistorialApuestas } from './historial-apuestas.model';
import { SelectItem } from 'primeng/api';

@Component({
  selector: 'jhi-historial-apuestas-page',
  templateUrl: './historial-apuestas-page.component.html',
  styleUrls: ['./historial-apuestas-page.component.scss', '../../../assets/styles1.css'],
})
export class HistorialApuestasPageComponent implements OnInit {
  userInfo: AmigoDetail | null = null;
  apuestas!: HistorialApuestas[];
  loading = true;
  apuestasPendientes: IApuesta[] = [];
  rendimiento = 0;
  resultados: SelectItem<string>[] = [];

  constructor(protected apuestasService: ApuestaService, private accountService: AccountService) {}

  loadApuestas(): void {
    this.loading = true;

    this.apuestasService.getApuestasFinalizadas().subscribe({
      next: (res: HttpResponse<HistorialApuestas[]>) => {
        this.apuestas = res.body ?? [];
      },
      error: () => {
        this.loading = false;
      },
    });

    this.apuestasService.getApuestasPendientes().subscribe({
      next: (res: HttpResponse<IApuesta[]>) => {
        this.apuestasPendientes = res.body ?? [];

        this.loading = false;
      },
      error: () => {
        this.loading = false;
      },
    });
  }

  ngOnInit(): void {
    this.accountService.getAuthenticatedInfo().subscribe(info => {
      this.userInfo = info;
      if (info.totales !== 0) {
        this.rendimiento = (info.ganados * 100) / info.totales;
      }
    });
    this.loadApuestas();

    this.resultados = [
      { label: 'Acertaste', value: 'Acertaste' },
      { label: 'Perdiste', value: 'Perdiste' },
    ];
  }

  clear(table: Table): void {
    table.clear();
  }
}
