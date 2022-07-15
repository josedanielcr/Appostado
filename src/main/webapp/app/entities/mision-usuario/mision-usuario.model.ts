import { IUsuario } from 'app/entities/usuario/usuario.model';
import { IMision } from 'app/entities/mision/mision.model';

export interface IMisionUsuario {
  id?: number;
  idMision?: number;
  idUsuario?: number;
  completado?: boolean;
  usuario?: IUsuario | null;
  mision?: IMision | null;
}

export class MisionUsuario implements IMisionUsuario {
  constructor(
    public id?: number,
    public idMision?: number,
    public idUsuario?: number,
    public completado?: boolean,
    public usuario?: IUsuario | null,
    public mision?: IMision | null
  ) {
    this.completado = this.completado ?? false;
  }
}

export function getMisionUsuarioIdentifier(misionUsuario: IMisionUsuario): number | undefined {
  return misionUsuario.id;
}
