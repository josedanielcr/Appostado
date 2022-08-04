import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IUsuario {
  id?: number;
  nombrePerfil?: string | null;
  pais?: string;
  fechaNacimiento?: dayjs.Dayjs;
  user?: IUser | null;
}

export class Usuario implements IUsuario {
  constructor(
    public id?: number,
    public nombrePerfil?: string | null,
    public pais?: string,
    public fechaNacimiento?: dayjs.Dayjs,
    public user?: IUser | null
  ) {}
}

export function getUsuarioIdentifier(usuario: IUsuario): number | undefined {
  return usuario.id;
}
