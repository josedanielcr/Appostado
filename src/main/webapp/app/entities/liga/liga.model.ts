export interface ILiga {
  id?: number;
  nombre?: string;
  descripcion?: string | null;
  foto?: string | null;
}

export class Liga implements ILiga {
  constructor(public id?: number, public nombre?: string, public descripcion?: string | null, public foto?: string | null) {}
}

export function getLigaIdentifier(liga: ILiga): number | undefined {
  return liga.id;
}
