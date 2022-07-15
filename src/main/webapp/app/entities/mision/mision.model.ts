export interface IMision {
  id?: number;
  nombre?: string;
  descripcion?: string;
  bonoCreditos?: number;
  dia?: string;
}

export class Mision implements IMision {
  constructor(public id?: number, public nombre?: string, public descripcion?: string, public bonoCreditos?: number, public dia?: string) {}
}

export function getMisionIdentifier(mision: IMision): number | undefined {
  return mision.id;
}
