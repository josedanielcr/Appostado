import { IMision } from 'app/entities/mision/mision.model';
import { ITransaccion } from 'app/entities/transaccion/transaccion.model';

export interface IMisionTransaccion {
  id?: number;
  mision?: IMision | null;
  transaccion?: ITransaccion | null;
}

export class MisionTransaccion implements IMisionTransaccion {
  constructor(public id?: number, public mision?: IMision | null, public transaccion?: ITransaccion | null) {}
}

export function getMisionTransaccionIdentifier(misionTransaccion: IMisionTransaccion): number | undefined {
  return misionTransaccion.id;
}
