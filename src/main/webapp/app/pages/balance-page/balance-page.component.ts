import { Component, OnInit } from '@angular/core';
import { BalancePageService } from './balance-page.service';
import { AmigoDetail } from '../amigos-page/amigos-page.model';
import { AccountService } from '../../core/auth/account.service';
import { ITransaccion } from '../../entities/transaccion/transaccion.model';
import { HttpResponse } from '@angular/common/http';
import { Table } from 'primeng/table';
import { SelectItem } from 'primeng/api';

@Component({
  selector: 'jhi-balance-page',
  templateUrl: './balance-page.component.html',
  styleUrls: ['./balance-page.component.scss', '../../../assets/styles1.css'],
})
export class BalancePageComponent implements OnInit {
  userInfo: AmigoDetail | null = null;
  transaccions!: ITransaccion[];
  loading = true;
  tipos: SelectItem<string>[] = [];

  constructor(protected balancePageService: BalancePageService, private accountService: AccountService) {}

  loadTransaccions(): void {
    this.loading = true;

    this.balancePageService.getTransacciones().subscribe({
      next: (res: HttpResponse<ITransaccion[]>) => {
        this.transaccions = res.body ?? [];
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
      { label: 'Crédito', value: 'Crédito' },
      { label: 'Débito', value: 'Débito' },
      { label: 'Bono', value: 'Bono' },
      { label: 'Misión', value: 'Misión' },
      { label: 'Canje', value: 'Canje' },
    ];
  }

  clear(table: Table): void {
    table.clear();
  }
}
