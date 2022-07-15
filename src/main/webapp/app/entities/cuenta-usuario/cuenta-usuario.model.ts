export interface ICuentaUsuario {
  id?: number;
  balance?: number;
  numCanjes?: number;
  apuestasTotales?: number;
  apuestasGanadas?: number;
}

export class CuentaUsuario implements ICuentaUsuario {
  constructor(
    public id?: number,
    public balance?: number,
    public numCanjes?: number,
    public apuestasTotales?: number,
    public apuestasGanadas?: number
  ) {}
}

export function getCuentaUsuarioIdentifier(cuentaUsuario: ICuentaUsuario): number | undefined {
  return cuentaUsuario.id;
}
