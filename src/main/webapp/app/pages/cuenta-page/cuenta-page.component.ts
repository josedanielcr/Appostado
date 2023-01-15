import { Component, OnInit } from '@angular/core';
import { IUsuario } from '../../entities/usuario/usuario.model';
import { UsuarioService } from '../../entities/usuario/service/usuario.service';
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
        this.cuentaUsuarioService.findByUserID(this.usuario.id!).subscribe({
          next: res => {
            this.cuentaUsuario = res.body!;
          },
        });
      },
    });
  }
}
