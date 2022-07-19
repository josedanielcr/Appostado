import dayjs from 'dayjs/esm';
import { IDeporte } from 'app/entities/deporte/deporte.model';
import { IDivision } from 'app/entities/division/division.model';
import { ICompetidor } from 'app/entities/competidor/competidor.model';

export interface IEvento {
  id?: number;
  idGanador?: number | null;
  marcador1?: number | null;
  marcador2?: number | null;
  estado?: string;
  multiplicador?: number;
  fecha?: dayjs.Dayjs;
  horaInicio?: dayjs.Dayjs;
  horaFin?: dayjs.Dayjs;
  deporte?: IDeporte | null;
  division?: IDivision | null;
  competidor1?: ICompetidor | null;
  competidor2?: ICompetidor | null;
}

export class Evento implements IEvento {
  constructor(
    public id?: number,
    public idGanador?: number | null,
    public marcador1?: number | null,
    public marcador2?: number | null,
    public estado?: string,
    public multiplicador?: number,
    public fecha?: dayjs.Dayjs,
    public horaInicio?: dayjs.Dayjs,
    public horaFin?: dayjs.Dayjs,
    public deporte?: IDeporte | null,
    public division?: IDivision | null,
    public competidor1?: ICompetidor | null,
    public competidor2?: ICompetidor | null
  ) {}
}

export function getEventoIdentifier(evento: IEvento): number | undefined {
  return evento.id;
}
