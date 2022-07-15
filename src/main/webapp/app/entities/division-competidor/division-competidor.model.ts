import { IDivision } from 'app/entities/division/division.model';
import { ICompetidor } from 'app/entities/competidor/competidor.model';

export interface IDivisionCompetidor {
  id?: number;
  idDivision?: number;
  idCompetidor?: number;
  division?: IDivision | null;
  competidor?: ICompetidor | null;
}

export class DivisionCompetidor implements IDivisionCompetidor {
  constructor(
    public id?: number,
    public idDivision?: number,
    public idCompetidor?: number,
    public division?: IDivision | null,
    public competidor?: ICompetidor | null
  ) {}
}

export function getDivisionCompetidorIdentifier(divisionCompetidor: IDivisionCompetidor): number | undefined {
  return divisionCompetidor.id;
}
