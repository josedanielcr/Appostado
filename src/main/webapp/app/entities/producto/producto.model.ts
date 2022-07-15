export interface IProducto {
  id?: number;
  nombre?: string;
  descripcion?: string;
  costo?: number;
  foto?: string;
  codigoFijo?: string | null;
  numCompras?: number | null;
}

export class Producto implements IProducto {
  constructor(
    public id?: number,
    public nombre?: string,
    public descripcion?: string,
    public costo?: number,
    public foto?: string,
    public codigoFijo?: string | null,
    public numCompras?: number | null
  ) {}
}

export function getProductoIdentifier(producto: IProducto): number | undefined {
  return producto.id;
}
