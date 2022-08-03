import { Component, OnInit } from '@angular/core';
import { IUsuario } from '../../entities/usuario/usuario.model';
import { EntityResponseType, UsuarioService } from '../../entities/usuario/service/usuario.service';
import { HttpResponse } from '@angular/common/http';
import { IDivision } from '../../entities/division/division.model';
import { Observable } from 'rxjs';
import { ICuentaUsuario } from '../../entities/cuenta-usuario/cuenta-usuario.model';
import { CuentaUsuarioService } from '../../entities/cuenta-usuario/service/cuenta-usuario.service';

@Component({
  selector: 'jhi-cuenta-page',
  templateUrl: './cuenta-page.component.html',
  styleUrls: ['./cuenta-page.component.scss'],
})
export class CuentaPageComponent implements OnInit {
  usuario?: IUsuario;
  id?: number;
  cuentaUsuario?: ICuentaUsuario;

  constructor(protected usuarioServicio: UsuarioService, protected cuentaUsuarioService: CuentaUsuarioService) {
    return;
  }

  ngOnInit(): void {
    this.loadDatosPerfil();
    return;
  }

  loadDatosPerfil(): void {
    this.usuarioServicio.getUsuarioLogueado().subscribe({
      next: r => {
        this.usuario = r.body!;
        const fecha = r.body?.fechaNacimiento?.toDate().toDateString();
        this.usuario.nacimiento = fecha;
        this.cuentaUsuarioService.findByUserID(this.usuario.id!).subscribe({
          next: res => {
            this.cuentaUsuario = res.body!;
          },
        });
      },
    });
  }
}
