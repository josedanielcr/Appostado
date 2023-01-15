import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { IMisionUsuario } from '../../entities/mision-usuario/mision-usuario.model';
import { IMision } from '../../entities/mision/mision.model';
import { MisionUsuarioService } from '../../entities/mision-usuario/service/mision-usuario.service';
import { MisionService } from '../../entities/mision/service/mision.service';
import { FormGroup, FormControl } from '@angular/forms';
import { Validators } from '@angular/forms';
import Swal from 'sweetalert2';

@Component({
  selector: 'jhi-misiones-page',
  templateUrl: './misiones-page.component.html',
  styleUrls: ['../../../assets/styles1.css'],
})
export class MisionesPageComponent implements OnInit {
  misionUsuarios?: IMisionUsuario[];

  misionesTrivias?: IMision[];
  misionesPublicidades?: IMision[];
  isLoading = false;
  respuesta = false;

  triviaForm = new FormGroup({
    respuesta: new FormControl('', Validators.required),
  });

  constructor(protected misionUsuarioService: MisionUsuarioService, protected misionService: MisionService) {}

  loadAll(): void {
    this.isLoading = true;

    this.misionUsuarioService.query().subscribe({
      next: (res: HttpResponse<IMisionUsuario[]>) => {
        this.isLoading = false;
        this.misionUsuarios = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  loadTrivias(): void {
    this.isLoading = true;
    this.misionService.getTrivias().subscribe({
      next: (res: HttpResponse<IMision[]>) => {
        this.isLoading = false;
        this.misionesTrivias = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  loadPublicidad(): void {
    this.isLoading = true;
    this.misionService.getPublicidad().subscribe({
      next: (res: HttpResponse<IMision[]>) => {
        this.isLoading = false;
        this.misionesPublicidades = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadTrivias();
    this.loadPublicidad();
  }

  trackId(_index: number, item: IMisionUsuario): number {
    return item.id!;
  }

  onSubmit(idMision: any): void {
    const respuestaT = this.triviaForm.get(['respuesta'])!.value;
    this.misionService.resolverTrivia(idMision, respuestaT).subscribe(
      data => {
        this.respuesta = data;
        if (this.respuesta === true) {
          Swal.fire({
            icon: 'success',
            title: 'Respuesta correcta',

            confirmButtonColor: '#38b000',
          });
          this.loadTrivias();
          this.loadPublicidad();
        } else {
          Swal.fire({
            icon: 'error',
            title: 'Oopss... respuesta incorrecta',
            confirmButtonColor: '#38b000',
            timer: 10000,
          });
          this.loadTrivias();
          this.loadPublicidad();
        }
      },
      error => {
        Swal.fire({
          icon: 'error',
          title: 'Oopss...',
          text: error,
          confirmButtonColor: '#38b000',
          timer: 10000,
        });

        this.loadTrivias();
        this.loadPublicidad();
      }
    );
  }

  publicidad(id: any): void {
    this.misionService.resolverPublicidad(id).subscribe(
      data => {
        Swal.fire({
          icon: 'success',
          title: 'Gracias por visitar la página del enlace',
          text: 'Revisa tus créditos',
          confirmButtonColor: '#38b000',
        });
        console.log(data);
        this.loadTrivias();
        this.loadPublicidad();
      },
      error => {
        Swal.fire({
          icon: 'error',
          title: 'Oopss...',
          text: error,
          confirmButtonColor: '#38b000',
          timer: 10000,
        });

        this.loadTrivias();
        this.loadPublicidad();
      }
    );
  }
}
