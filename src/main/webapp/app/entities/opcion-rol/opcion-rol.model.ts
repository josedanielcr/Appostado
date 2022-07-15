export interface IOpcionRol {
  id?: number;
  opcion?: string;
  path?: string;
  icono?: string;
  nombre?: string;
}

export class OpcionRol implements IOpcionRol {
  constructor(public id?: number, public opcion?: string, public path?: string, public icono?: string, public nombre?: string) {}
}

export function getOpcionRolIdentifier(opcionRol: IOpcionRol): number | undefined {
  return opcionRol.id;
}
