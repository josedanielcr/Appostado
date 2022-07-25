import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { CuentaUsuarioService } from '../../entities/cuenta-usuario/service/cuenta-usuario.service';
import { IRanking } from '../../entities/cuenta-usuario/ranking-model';
import { FilterMatchMode, PrimeNGConfig } from 'primeng/api';

@Component({
  selector: 'jhi-ranking-page',
  templateUrl: './ranking-page.component.html',
  styleUrls: ['./ranking-page.component.scss'],
})
export class RankingPageComponent implements OnInit {
  rankings?: IRanking[];
  isLoading = false;
  first: any = 0;

  constructor(protected cuentaUsuarioService: CuentaUsuarioService, private config: PrimeNGConfig) {}

  loadAll(): void {
    this.isLoading = true;

    this.cuentaUsuarioService.ranking().subscribe({
      next: (res: HttpResponse<IRanking[]>) => {
        this.isLoading = false;
        this.rankings = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();

    this.config.filterMatchModeOptions = {
      text: [
        FilterMatchMode.STARTS_WITH,
        FilterMatchMode.CONTAINS,
        FilterMatchMode.NOT_CONTAINS,
        FilterMatchMode.ENDS_WITH,
        FilterMatchMode.EQUALS,
        FilterMatchMode.NOT_EQUALS,
      ],
      numeric: [
        FilterMatchMode.EQUALS,
        FilterMatchMode.NOT_EQUALS,
        FilterMatchMode.LESS_THAN,
        FilterMatchMode.LESS_THAN_OR_EQUAL_TO,
        FilterMatchMode.GREATER_THAN,
        FilterMatchMode.GREATER_THAN_OR_EQUAL_TO,
      ],
      date: [FilterMatchMode.DATE_IS, FilterMatchMode.DATE_IS_NOT, FilterMatchMode.DATE_BEFORE, FilterMatchMode.DATE_AFTER],
    };
  }
}
