import dayjs from 'dayjs/esm';

export class HistorialApuestas {
  constructor(
    public resultado?: string,
    public descripcion?: string,
    public fecha?: dayjs.Dayjs,
    public estado?: string,
    public creditos?: number,
    public apostado?: string
  ) {}
}
