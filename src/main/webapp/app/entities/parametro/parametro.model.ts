export interface IParametro {
  id?: number;
  telefono?: string | null;
  correo?: string | null;
  direccion?: string | null;
}

export class Parametro implements IParametro {
  constructor(public id?: number, public telefono?: string | null, public correo?: string | null, public direccion?: string | null) {}
}

export function getParametroIdentifier(parametro: IParametro): number | undefined {
  return parametro.id;
}
