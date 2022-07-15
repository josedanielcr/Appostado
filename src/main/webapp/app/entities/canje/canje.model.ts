import { IPremio } from 'app/entities/premio/premio.model';
import { ITransaccion } from 'app/entities/transaccion/transaccion.model';

export interface ICanje {
  id?: number;
  idPremio?: number;
  idTransaccion?: number;
  premio?: IPremio | null;
  transaccion?: ITransaccion | null;
}

export class Canje implements ICanje {
  constructor(
    public id?: number,
    public idPremio?: number,
    public idTransaccion?: number,
    public premio?: IPremio | null,
    public transaccion?: ITransaccion | null
  ) {}
}

export function getCanjeIdentifier(canje: ICanje): number | undefined {
  return canje.id;
}
