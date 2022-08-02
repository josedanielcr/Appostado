import { Component, OnInit, ViewChild } from '@angular/core';
import { BalancePageService } from './balance-page.service';
import { AmigoDetail } from '../amigos-page/amigos-page.model';
import { AccountService } from '../../core/auth/account.service';
import { ITransaccion } from '../../entities/transaccion/transaccion.model';
import { HttpResponse } from '@angular/common/http';
import { Table } from 'primeng/table';

@Component({
  selector: 'jhi-balance-page',
  templateUrl: './balance-page.component.html',
  styleUrls: ['./balance-page.component.scss', '../../../assets/styles1.css'],
})
export class BalancePageComponent implements OnInit {
  userInfo: AmigoDetail | null = null;
  transaccions!: ITransaccion[];
  loading = true;
  tipos: any[];
  @ViewChild('dt') dt: Table | undefined;

  constructor(protected balancePageService: BalancePageService, private accountService: AccountService) {}

  loadTransaccions(): void {
    this.loading = true;

    this.balancePageService.getTransacciones().subscribe({
      next: (res: HttpResponse<ITransaccion[]>) => {
        this.transaccions = res.body ?? [];
        console.log(this.transaccions);
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
    });
    this.loadTransaccions();

    this.tipos = [
      { label: 'Credito', value: 'Credito' },
      { label: 'Debito', value: 'Debito' },
      { label: 'Bono', value: 'Bono' },
      { label: 'Mision', value: 'Mision' },
      { label: 'Canje', value: 'Canje' },
    ];
  }

  clear(table: Table): void {
    table.clear();
  }

  applyFilterGlobal($event: any, stringVal: any): void {
    this.dt!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
  }
}
