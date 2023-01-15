export interface IDeporte {
  id?: number;
  nombre?: string;
}

export class Deporte implements IDeporte {
  constructor(public id?: number, public nombre?: string) {}
}

export function getDeporteIdentifier(deporte: IDeporte): number | undefined {
  return deporte.id;
}
