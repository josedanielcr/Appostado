import { IApuesta } from 'app/entities/apuesta/apuesta.model';
import { ITransaccion } from 'app/entities/transaccion/transaccion.model';

export interface IApuestaTransaccion {
  id?: number;
  idApuesta?: number;
  idTransaccion?: number;
  apuesta?: IApuesta | null;
  transaccion?: ITransaccion | null;
}

export class ApuestaTransaccion implements IApuestaTransaccion {
  constructor(
    public id?: number,
    public idApuesta?: number,
    public idTransaccion?: number,
    public apuesta?: IApuesta | null,
    public transaccion?: ITransaccion | null
  ) {}
}

export function getApuestaTransaccionIdentifier(apuestaTransaccion: IApuestaTransaccion): number | undefined {
  return apuestaTransaccion.id;
}
