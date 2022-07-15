import dayjs from 'dayjs/esm';
import { IUsuario } from 'app/entities/usuario/usuario.model';

export interface INotificacion {
  id?: number;
  idUsuario?: number;
  descripcion?: string;
  tipo?: string;
  fecha?: dayjs.Dayjs;
  haGanado?: boolean | null;
  ganancia?: number | null;
  fueLeida?: boolean | null;
  usuario?: IUsuario | null;
}

export class Notificacion implements INotificacion {
  constructor(
    public id?: number,
    public idUsuario?: number,
    public descripcion?: string,
    public tipo?: string,
    public fecha?: dayjs.Dayjs,
    public haGanado?: boolean | null,
    public ganancia?: number | null,
    public fueLeida?: boolean | null,
    public usuario?: IUsuario | null
  ) {
    this.haGanado = this.haGanado ?? false;
    this.fueLeida = this.fueLeida ?? false;
  }
}

export function getNotificacionIdentifier(notificacion: INotificacion): number | undefined {
  return notificacion.id;
}
