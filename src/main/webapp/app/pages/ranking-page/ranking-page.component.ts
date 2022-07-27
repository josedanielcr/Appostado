import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { CuentaUsuarioService } from '../../entities/cuenta-usuario/service/cuenta-usuario.service';
import { IRanking } from '../../entities/cuenta-usuario/ranking-model';
import { FilterMatchMode, PrimeNGConfig } from 'primeng/api';
import { FormBuilder, Validators } from '@angular/forms';
import { Table } from 'primeng/table';
import { AccountService } from '../../core/auth/account.service';
import { Account } from '../../core/auth/account.model';
import { Observable } from 'rxjs';

@Component({
  selector: 'jhi-ranking-page',
  templateUrl: './ranking-page.component.html',
  styleUrls: ['./ranking-page.component.scss'],
})
export class RankingPageComponent implements OnInit {
  rankings?: IRanking[];
  rankingPersonalDatos?: IRanking[];
  nacionalidad?: string;
  isLoading = false;
  first: any = 0;
  opcionFiltroRanking?: any[];
  editForm = this.fb.group({
    filtro: [],
  });

  private userIdentity: Account | null = null;

  constructor(
    protected fb: FormBuilder,
    protected cuentaUsuarioService: CuentaUsuarioService,
    private config: PrimeNGConfig,
    private servicioCuenta: AccountService
  ) {}

  loadAll(): void {
    this.isLoading = true;
    this.opcionFiltroRanking = [
      { nombre: 'Global', id: 1 },
      { nombre: 'Nacional', id: 2 },
    ];

    this.rankingGlobal();
    this.rankingPersonal();
  }

  rankingGlobal(): void {
    this.cuentaUsuarioService.rankingGlobal().subscribe({
      next: (res: HttpResponse<IRanking[]>) => {
        this.isLoading = false;
        this.rankings = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  rankingNacional(): void {
    this.cuentaUsuarioService.rankingNacional(this.nacionalidad!).subscribe({
      next: (res: HttpResponse<IRanking[]>) => {
        this.isLoading = false;
        this.rankings = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  rankingPersonal(): void {
    this.cuentaUsuarioService.rankingPersonal().subscribe({
      next: (res: HttpResponse<IRanking[]>) => {
        this.isLoading = false;
        this.rankingPersonalDatos = res.body ?? [];
        this.nacionalidad = res.body![0].nacionalidad;
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

  cambiarRanking(table: Table): void {
    const filtro = this.editForm.get(['filtro'])!.value;
    if (filtro.id === 2) {
      this.rankings = [];
      this.rankingNacional();
    } else {
      this.rankingGlobal();
    }
  }
}
