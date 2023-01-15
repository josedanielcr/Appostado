import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface IAmigo {
  id?: number;
  usuario?: IUsuario | null;
  amigo?: IUsuario | null;
}

export class Amigo implements IAmigo {
  constructor(public id?: number, public usuario?: IUsuario | null, public amigo?: IUsuario | null) {}
}

export function getAmigoIdentifier(amigo: IAmigo): number | undefined {
  return amigo.id;
}
