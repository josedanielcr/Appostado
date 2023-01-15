export interface ICompetidor {
  id?: number;
  nombre?: string;
  foto?: string;
}

export class Competidor implements ICompetidor {
  constructor(public id?: number, public nombre?: string, public foto?: string) {}
}

export function getCompetidorIdentifier(competidor: ICompetidor): number | undefined {
  return competidor.id;
}
