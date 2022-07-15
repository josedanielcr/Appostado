import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { ICuentaUsuario } from 'app/entities/cuenta-usuario/cuenta-usuario.model';

export interface IUsuario {
  id?: number;
  idCuenta?: number;
  nombreUsuario?: string;
  nombrePerfil?: string | null;
  pais?: string;
  fechaNacimiento?: dayjs.Dayjs;
  user?: IUser | null;
  cuenta?: ICuentaUsuario | null;
}

export class Usuario implements IUsuario {
  constructor(
    public id?: number,
    public idCuenta?: number,
    public nombreUsuario?: string,
    public nombrePerfil?: string | null,
    public pais?: string,
    public fechaNacimiento?: dayjs.Dayjs,
    public user?: IUser | null,
    public cuenta?: ICuentaUsuario | null
  ) {}
}

export function getUsuarioIdentifier(usuario: IUsuario): number | undefined {
  return usuario.id;
}
