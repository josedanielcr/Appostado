export interface IPremio {
  id?: number;
  nombre?: string | null;
  descripcion?: string | null;
  foto?: string | null;
  costo?: number | null;
  estado?: string | null;
  stock?: number | null;
  numCanjes?: number | null;
}

export class Premio implements IPremio {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public descripcion?: string | null,
    public foto?: string | null,
    public costo?: number | null,
    public estado?: string | null,
    public stock?: number | null,
    public numCanjes?: number | null
  ) {}
}

export function getPremioIdentifier(premio: IPremio): number | undefined {
  return premio.id;
}
