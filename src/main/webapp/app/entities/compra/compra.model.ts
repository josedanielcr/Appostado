import { ITransaccion } from 'app/entities/transaccion/transaccion.model';
import { IProducto } from 'app/entities/producto/producto.model';

export interface ICompra {
  id?: number;
  transaccion?: ITransaccion | null;
  producto?: IProducto | null;
}

export class Compra implements ICompra {
  constructor(public id?: number, public transaccion?: ITransaccion | null, public producto?: IProducto | null) {}
}

export function getCompraIdentifier(compra: ICompra): number | undefined {
  return compra.id;
}
