export interface IQuiniela {
  id?: number;
  nombre?: string;
  descripcion?: string | null;
  costoParticipacion?: number;
  primerPremio?: number;
  segundoPremio?: number;
  tercerPremio?: number;
  estado?: string;
}

export class Quiniela implements IQuiniela {
  constructor(
    public id?: number,
    public nombre?: string,
    public descripcion?: string | null,
    public costoParticipacion?: number,
    public primerPremio?: number,
    public segundoPremio?: number,
    public tercerPremio?: number,
    public estado?: string
  ) {}
}

export function getQuinielaIdentifier(quiniela: IQuiniela): number | undefined {
  return quiniela.id;
}
