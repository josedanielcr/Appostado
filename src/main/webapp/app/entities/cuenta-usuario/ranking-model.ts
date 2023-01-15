export interface IRanking {
  posicion?: number;
  recordNeto?: number;
  nombreJugador?: string;
  nacionalidad?: string;
  totalGanadas?: number;
  totalPerdidas?: number;
  totalCanjes?: number;
  rendimiento?: number;
  foto?: string;
}

export class Ranking implements IRanking {
  constructor(
    public posicion?: number,
    public recordNeto?: number,
    public nombreJugador?: string,
    public nacionalidad?: string,
    public totalGanadas?: number,
    public totalPerdidas?: number,
    public totalCanjes?: number,
    public rendimiento?: number,
    public foto?: string
  ) {}
}

export function getRankingID(ranking: IRanking): number | undefined {
  return ranking.posicion;
}
