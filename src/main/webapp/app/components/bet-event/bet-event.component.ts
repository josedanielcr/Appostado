import { Component, Input, OnInit } from '@angular/core';
import { IEventCalculatedData, IEvento } from '../../entities/evento/evento.model';
import { CuentaUsuarioService } from '../../entities/cuenta-usuario/service/cuenta-usuario.service';
import { HttpResponse } from '@angular/common/http';
import { ICuentaUsuario } from '../../entities/cuenta-usuario/cuenta-usuario.model';
import { FormBuilder, FormGroup } from '@angular/forms';
import { EventoService } from '../../entities/evento/service/evento.service';
import { IApuesta } from '../../entities/apuesta/apuesta.model';
import { ApuestaService } from '../../entities/apuesta/service/apuesta.service';
import Swal, { SweetAlertResult } from 'sweetalert2';

@Component({
  selector: 'jhi-bet-event',
  templateUrl: './bet-event.component.html',
  styleUrls: ['./bet-event.component.scss'],
})
export class BetEventComponent implements OnInit {
  @Input() event: IEvento | null;
  public eventCalcData: IEventCalculatedData | null;
  public hasTie = false;
  public availableBalance: number;
  public betError = '';
  public betForm: FormGroup;
  public sliderVal: number;
  public isAlreadyParticipating = false;

  constructor(
    private fb: FormBuilder,
    private userAccountService: CuentaUsuarioService,
    private eventService: EventoService,
    private betService: ApuestaService
  ) {
    return;
  }

  ngOnInit(): void {
    this.getUserData();
    this.checkIfEventHasTie();
    this.createForm();
    this.getEventCalculatedData();
  }

  public submitForm(): void {
    if (this.betForm.invalid) {
      this.betError = 'Es necesario ingresar todos los datos para crear una apuesta';
      return;
    }

    if (!this.betFormValidations()) {
      this.betError = 'Ingrese la información de la apuesta correctamente';
      return;
    }

    /*Creacion de objeto de apuesta para enviar al backend*/
    const apuesta: IApuesta = {
      creditosApostados: this.betForm.controls['creditosApostados'].value,
      haGanado: null,
      estado: 'Pendiente',
      usuario: null,
      evento: this.event,
    };

    if (this.betForm.controls['competidor1'].value) {
      apuesta.apostado = this.event?.competidor1;
    }
    if (this.betForm.controls['competidor2'].value) {
      apuesta.apostado = this.event?.competidor2;
    }
    this.createBet(apuesta);
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
        this.betService.getApuestasByEventId(this.event!.id!.toString()).subscribe({
          next: (res: HttpResponse<IApuesta[]>) => {
            res.body?.forEach((apuesta: IApuesta) => {
              if (apuesta.usuario?.id === account.body?.usuario?.id) {
                this.isAlreadyParticipating = true;
                return;
              }
            });
          },
        });
        this.availableBalance = account.body!.balance!;
        const input: HTMLInputElement | null = document.querySelector('#balanceInput');
        input?.setAttribute('value', `Créditos disponibles: ${this.availableBalance.toString()}`);
      },
      error: () => {
        this.betError = 'Ha ocurrido un error, intente nuevamente';
      },
    });
  }

  private getEventCalculatedData(): void {
    this.eventService.getEventCalculatedData().subscribe({
      next: (res: HttpResponse<IEventCalculatedData>) => {
        this.eventCalcData = res.body;
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

  private createBet(apuesta: IApuesta): void {
    Swal.fire({
      title: 'Desea realizar la apuesta por ' + String(apuesta.creditosApostados) + ' créditos?',
      showDenyButton: true,
      showCancelButton: false,
      confirmButtonText: 'Apostar',
      denyButtonText: `Cancelar`,
    }).then((result: SweetAlertResult<Awaited<any>>) => {
      if (result.isConfirmed) {
        this.betService.create(apuesta).subscribe({
          next: () => {
            Swal.fire({
              title: 'Apuesta creada',
              text:
                'Se han apostado los siguientes créditos: ' +
                String(apuesta.creditosApostados) +
                ' al competidor: ' +
                String(apuesta.apostado?.nombre),
              icon: 'success',
              position: 'bottom-end',
              timer: 2500,
            });
            setTimeout(() => {
              history.back();
            }, 3000);
          },
          error: (err: Error) => {
            this.betError = err.message;
            console.log(err);
          },
        });
      } else if (result.isDenied) {
        return;
      }
    });
  }

  private betFormValidations(): boolean {
    if (
      !(this.betForm.controls['competidor1'].value || this.betForm.controls['competidor2'].value || this.betForm.controls['empate'].value)
    ) {
      return false;
    }
    if (this.betForm.controls['competidor1'].value && this.betForm.controls['competidor2'].value) {
      return false;
    }
    if (
      (this.betForm.controls['empate'].value && this.betForm.controls['competidor1'].value) ||
      (this.betForm.controls['empate'].value && this.betForm.controls['competidor2'].value)
    ) {
      return false;
    }
    if (this.betForm.controls['creditosApostados'].value < 1) {
      return false;
    }
    return true;
  }
}
