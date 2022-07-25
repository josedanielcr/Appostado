import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subject } from 'rxjs';
import { CuentaUsuarioService } from '../../entities/cuenta-usuario/service/cuenta-usuario.service';
import { IRanking } from '../../entities/cuenta-usuario/ranking-model';

@Component({
  selector: 'jhi-ranking-page',
  templateUrl: './ranking-page.component.html',
  styleUrls: ['./ranking-page.component.scss'],
})
export class RankingPageComponent implements OnInit {
  dtOptions: DataTables.Settings = {};
  dtTrigger: Subject<any> = new Subject<any>();
  rankings?: IRanking[];
  isLoading = false;

  constructor(protected cuentaUsuarioService: CuentaUsuarioService) {}

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
    this.dtOptions = {
      pagingType: 'full_numbers',
      pageLength: 2,
    };
    this.loadAll();
  }

  ngOnDestroy(): void {
    // Do not forget to unsubscribe the event
    this.dtTrigger.unsubscribe();
  }
}
