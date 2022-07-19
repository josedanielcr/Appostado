import { IUsuario } from 'app/entities/usuario/usuario.model';
import { ILiga } from 'app/entities/liga/liga.model';

export interface ILigaUsuario {
  id?: number;
  usuario?: IUsuario | null;
  liga?: ILiga | null;
}

export class LigaUsuario implements ILigaUsuario {
  constructor(public id?: number, public usuario?: IUsuario | null, public liga?: ILiga | null) {}
}

export function getLigaUsuarioIdentifier(ligaUsuario: ILigaUsuario): number | undefined {
  return ligaUsuario.id;
}
