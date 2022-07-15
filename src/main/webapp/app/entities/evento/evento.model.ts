import dayjs from 'dayjs/esm';
import { IDeporte } from 'app/entities/deporte/deporte.model';
import { IDivision } from 'app/entities/division/division.model';
import { ICompetidor } from 'app/entities/competidor/competidor.model';
import { IQuiniela } from 'app/entities/quiniela/quiniela.model';

export interface IEvento {
  id?: number;
  idDeporte?: number;
  idDivision?: number;
  idCompetidor1?: number;
  idCompetidor2?: number;
  idQuiniela?: number | null;
  idGanador?: number;
  marcador1?: number;
  marcador2?: number;
  estado?: string;
  multiplicador?: number;
  fecha?: dayjs.Dayjs;
  horaInicio?: dayjs.Dayjs;
  horaFin?: dayjs.Dayjs;
  deporte?: IDeporte | null;
  division?: IDivision | null;
  competidor1?: ICompetidor | null;
  competidor2?: ICompetidor | null;
  quiniela?: IQuiniela | null;
}

export class Evento implements IEvento {
  constructor(
    public id?: number,
    public idDeporte?: number,
    public idDivision?: number,
    public idCompetidor1?: number,
    public idCompetidor2?: number,
    public idQuiniela?: number | null,
    public idGanador?: number,
    public marcador1?: number,
    public marcador2?: number,
    public estado?: string,
    public multiplicador?: number,
    public fecha?: dayjs.Dayjs,
    public horaInicio?: dayjs.Dayjs,
    public horaFin?: dayjs.Dayjs,
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
