import { ICompetidor } from 'app/entities/competidor/competidor.model';
import { IUsuario } from 'app/entities/usuario/usuario.model';
import { IEvento } from 'app/entities/evento/evento.model';

export interface IApuesta {
  id?: number;
  creditosApostados?: number;
  haGanado?: boolean | null;
  estado?: string;
  apostado?: ICompetidor | null;
  usuario?: IUsuario | null;
  evento?: IEvento | null;
}

export class Apuesta implements IApuesta {
  constructor(
    public id?: number,
    public creditosApostados?: number,
    public haGanado?: boolean | null,
    public estado?: string,
    public apostado?: ICompetidor | null,
    public usuario?: IUsuario | null,
    public evento?: IEvento | null
  ) {
    this.haGanado = this.haGanado ?? false;
  }
}

export function getApuestaIdentifier(apuesta: IApuesta): number | undefined {
  return apuesta.id;
}
