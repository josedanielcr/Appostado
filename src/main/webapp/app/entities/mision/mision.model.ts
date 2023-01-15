export interface IMision {
  id?: number;
  nombre?: string;
  descripcion?: string;
  bonoCreditos?: number;
  dia?: string;
  tipo?: string;
  opcion1?: string | null;
  opcion2?: string | null;
  opcion3?: string | null;
  opcion4?: string | null;
  enlace?: string | null;
  opcionCorrecta?: number | null;
}

export class Mision implements IMision {
  constructor(
    public id?: number,
    public nombre?: string,
    public descripcion?: string,
    public bonoCreditos?: number,
    public dia?: string,
    public tipo?: string,
    public opcion1?: string | null,
    public opcion2?: string | null,
    public opcion3?: string | null,
    public opcion4?: string | null,
    public enlace?: string | null,
    public opcionCorrecta?: number | null
  ) {}
}

export function getMisionIdentifier(mision: IMision): number | undefined {
  return mision.id;
}
