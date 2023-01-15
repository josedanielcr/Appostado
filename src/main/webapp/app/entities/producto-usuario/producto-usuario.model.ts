import { IProducto } from 'app/entities/producto/producto.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IProductoUsuario {
  id?: number;
  reclamado?: boolean;
  codigo?: string;
  producto?: IProducto | null;
  usuario?: IUsuario | null;
}

export class ProductoUsuario implements IProductoUsuario {
  constructor(
    public id?: number,
    public reclamado?: boolean,
    public codigo?: string,
    public producto?: IProducto | null,
    public usuario?: IUsuario | null
  ) {
    this.reclamado = this.reclamado ?? false;
  }
}

export function getProductoUsuarioIdentifier(productoUsuario: IProductoUsuario): number | undefined {
  return productoUsuario.id;
}
