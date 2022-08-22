import { Component, OnInit } from '@angular/core';
import { AmigoDetail } from '../amigos-page/amigos-page.model';
import { SelectItem } from 'primeng/api';
import { AccountService } from '../../core/auth/account.service';
import { HttpResponse } from '@angular/common/http';
import { Table } from 'primeng/table';
import { IApuesta } from '../../entities/apuesta/apuesta.model';
import { ApuestaService } from '../../entities/apuesta/service/apuesta.service';

@Component({
  selector: 'jhi-historial-apuestas-page',
  templateUrl: './historial-apuestas-page.component.html',
  styleUrls: ['./historial-apuestas-page.component.scss', '../../../assets/styles1.css'],
})
export class HistorialApuestasPageComponent implements OnInit {
  userInfo: AmigoDetail | null = null;
  apuestas!: IApuesta[];
  loading = true;
  resultados: SelectItem<string>[] = [];

  constructor(protected apuestasService: ApuestaService, private accountService: AccountService) {}

  loadApuestas(): void {
    this.loading = true;

    this.apuestasService.getApuestasFinalizadas().subscribe({
      next: (res: HttpResponse<IApuesta[]>) => {
        this.apuestas = res.body ?? [];
        this.loading = false;
        console.log(this.apuestas);
      },
      error: () => {
        this.loading = false;
      },
    });
  }

  ngOnInit(): void {
    this.accountService.getAuthenticatedInfo().subscribe(info => {
      this.userInfo = info;
    });
    this.loadApuestas();

    this.resultados = [
      { label: 'Gana', value: 'Gana' },
      { label: 'Pierde', value: 'Pierde' },
    ];
  }

  clear(table: Table): void {
    table.clear();
  }
}
