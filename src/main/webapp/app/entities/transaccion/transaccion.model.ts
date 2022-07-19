import dayjs from 'dayjs/esm';
import { ICuentaUsuario } from 'app/entities/cuenta-usuario/cuenta-usuario.model';

export interface ITransaccion {
  id?: number;
  fecha?: dayjs.Dayjs;
  tipo?: string;
  descripcion?: string;
  monto?: number;
  cuenta?: ICuentaUsuario | null;
}

export class Transaccion implements ITransaccion {
  constructor(
    public id?: number,
    public fecha?: dayjs.Dayjs,
    public tipo?: string,
    public descripcion?: string,
    public monto?: number,
    public cuenta?: ICuentaUsuario | null
  ) {}
}

export function getTransaccionIdentifier(transaccion: ITransaccion): number | undefined {
  return transaccion.id;
}
