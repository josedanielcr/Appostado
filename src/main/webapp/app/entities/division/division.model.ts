export interface IDivision {
  id?: number;
  nombre?: string;
  region?: string;
}

export class Division implements IDivision {
  constructor(public id?: number, public nombre?: string, public region?: string) {}
}

export function getDivisionIdentifier(division: IDivision): number | undefined {
  return division.id;
}
