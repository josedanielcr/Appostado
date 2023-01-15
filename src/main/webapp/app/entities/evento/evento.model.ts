import dayjs from 'dayjs/esm';
import { ICompetidor } from 'app/entities/competidor/competidor.model';
import { IDeporte } from 'app/entities/deporte/deporte.model';
import { IDivision } from 'app/entities/division/division.model';
import { IQuiniela } from 'app/entities/quiniela/quiniela.model';

export interface IEvento {
  id?: number;
  marcador1?: number | null;
  marcador2?: number | null;
  estado?: string;
  multiplicador?: number;
  fecha?: dayjs.Dayjs;
  horaInicio?: dayjs.Dayjs;
  horaFin?: dayjs.Dayjs;
  ganador?: ICompetidor | null;
  deporte?: IDeporte | null;
  division?: IDivision | null;
  competidor1?: ICompetidor | null;
  competidor2?: ICompetidor | null;
  quiniela?: IQuiniela | null;
}

export interface IEventCalculatedData {
  ganaciaEstimada: number;
  multiplicadorCompetidor1: number;
  multiplicadorCompetidor2: number;
  multiplicadorEmpate: number;
}

export class Evento implements IEvento {
  constructor(
    public id?: number,
    public marcador1?: number | null,
    public marcador2?: number | null,
    public estado?: string,
    public multiplicador?: number,
    public fecha?: dayjs.Dayjs,
    public horaInicio?: dayjs.Dayjs,
    public horaFin?: dayjs.Dayjs,
    public ganador?: ICompetidor | null,
    public deporte?: IDeporte | null,
    public division?: IDivision | null,
    public competidor1?: ICompetidor | null,
    public competidor2?: ICompetidor | null,
    public quiniela?: IQuiniela | null
  ) {}
}

export function getEventoIdentifier(evento: IEvento): number | undefined {
  return evento.id;
}
