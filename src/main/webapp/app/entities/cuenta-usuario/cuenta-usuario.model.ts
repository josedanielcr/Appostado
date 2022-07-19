import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface ICuentaUsuario {
  id?: number;
  balance?: number;
  numCanjes?: number;
  apuestasTotales?: number;
  apuestasGanadas?: number;
  usuario?: IUsuario | null;
}

export class CuentaUsuario implements ICuentaUsuario {
  constructor(
    public id?: number,
    public balance?: number,
    public numCanjes?: number,
    public apuestasTotales?: number,
    public apuestasGanadas?: number,
    public usuario?: IUsuario | null
  ) {}
}

export function getCuentaUsuarioIdentifier(cuentaUsuario: ICuentaUsuario): number | undefined {
  return cuentaUsuario.id;
}
