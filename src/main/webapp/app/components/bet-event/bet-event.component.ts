import { Component, Input, OnInit } from '@angular/core';
import { IEvento } from '../../entities/evento/evento.model';
import { CuentaUsuarioService } from '../../entities/cuenta-usuario/service/cuenta-usuario.service';
import { HttpResponse } from '@angular/common/http';
import { ICuentaUsuario } from '../../entities/cuenta-usuario/cuenta-usuario.model';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'jhi-bet-event',
  templateUrl: './bet-event.component.html',
  styleUrls: ['./bet-event.component.scss'],
})
export class BetEventComponent implements OnInit {
  @Input() event: IEvento | null;
  public hasTie = false;
  public availableBalance: number;
  public betError = '';
  public betForm: FormGroup;
  public sliderVal: number;

  constructor(private fb: FormBuilder, private userAccountService: CuentaUsuarioService) {
    return;
  }

  ngOnInit(): void {
    this.getUserData();
    this.checkIfEventHasTie();
    this.createForm();
  }

  public submitForm(): void {
    if (this.betForm.invalid) {
      this.betError = 'Error durante el proceso de busqueda, intente nuevamente';
      return;
    }
  }

  private checkIfEventHasTie(): void {
    const eventSport: string | undefined = this.event?.deporte?.nombre;
    if (eventSport === 'Fútbol') {
      this.hasTie = true;
    }
  }

  private getUserData(): void {
    this.userAccountService.findByUser().subscribe({
      next: (account: HttpResponse<ICuentaUsuario>) => {
        this.availableBalance = account.body!.balance!;
        const input: HTMLInputElement | null = document.querySelector('#balanceInput');
        input?.setAttribute('value', `Créditos disponibles: ${this.availableBalance.toString()}`);
      },
      error: () => {
        this.betError = 'Ha ocurrido un error, intente nuevamente';
      },
    });
  }

  private createForm(): void {
    this.betForm = this.fb.group({
      creditosApostados: [''],
      competidor1: [''],
      competidor2: [''],
      empate: [''],
    });
  }
}
