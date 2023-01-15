export interface IPremio {
  id?: number;
  nombre?: string;
  descripcion?: string;
  foto?: string | null;
  costo?: number;
  estado?: string;
  stock?: number | null;
  numCanjes?: number | null;
}

export class Premio implements IPremio {
  constructor(
    public id?: number,
    public nombre?: string,
    public descripcion?: string,
    public foto?: string | null,
    public costo?: number,
    public estado?: string,
    public stock?: number | null,
    public numCanjes?: number | null
  ) {}
}

export function getPremioIdentifier(premio: IPremio): number | undefined {
  return premio.id;
}
