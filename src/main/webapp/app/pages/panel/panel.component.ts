import { Component, OnInit } from '@angular/core';
import { AccountService } from '../../core/auth/account.service';
import { Router } from '@angular/router';
import { ProductoUsuarioService } from '../../entities/producto-usuario/service/producto-usuario.service';
import Swal from 'sweetalert2';
import { FormGroup, FormControl, Validators } from '@angular/forms';
@Component({
  selector: 'jhi-panel',
  templateUrl: './panel.component.html',
  styleUrls: ['../../../assets/styles1.css'],
})
export class PanelComponent implements OnInit {
  respuesta = '';
  BonoForm = new FormGroup({
    codigo: new FormControl('', [Validators.required]),
  });
  constructor(private accountService: AccountService, private router: Router, private productoUsuarioService: ProductoUsuarioService) {
    return;
  }

  ngOnInit(): void {
    this.accountService.identity().subscribe(() => {
      if (!this.accountService.isAuthenticated()) {
        this.router.navigate(['/landing']);
      }
    });
  }

  realizarCanje(codigo: string): void {
    Swal.fire({
      icon: 'question',
      title: 'Estás seguro de que deseas realizar este canje?',
      showDenyButton: true,
      confirmButtonColor: '#38b000',
      confirmButtonText: `Si`,
      denyButtonText: `No`,
    }).then(result => {
      /* Read more about isConfirmed, isDenied below */
      if (result.isConfirmed) {
        this.productoUsuarioService.bono(codigo).subscribe(
          data => {
            this.respuesta = data;
            if (this.respuesta === 'si') {
              Swal.fire({
                icon: 'success',
                title: 'El canje se ha realizado con éxito.',
                confirmButtonColor: '#38b000',
              });
            }
            if (this.respuesta === 'no') {
              Swal.fire({
                icon: 'error',
                title: 'Oopss...',
                text: 'Este código ya ha sido utilizado.',
                confirmButtonColor: '#38b000',
                timer: 10000,
              });
            }
            if (this.respuesta === 'codInv') {
              Swal.fire({
                icon: 'error',
                title: 'Oopss...',
                text: 'Este código es inválido.',
                confirmButtonColor: '#38b000',
                timer: 10000,
              });
            }
          },
          error => {
            console.log(error);
          }
        );
      } else if (result.isDenied) {
        Swal.fire({
          icon: 'error',
          title: 'Canje cancelado',
          confirmButtonColor: '#38b000',
          timer: 10000,
        });
      }
    });
  }

  onSubmit(): void {
    const codigo = this.BonoForm.get(['codigo'])!.value;
    this.realizarCanje(codigo);
  }
}
