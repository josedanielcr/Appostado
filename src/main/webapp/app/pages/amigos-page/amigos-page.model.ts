import dayjs from 'dayjs/esm';

export class AmigoDetail {
  constructor(
    public login: string,
    public perfil: string,
    public balance: number,
    public totales: number,
    public ganados: number,
    public avatar: string,
    public country: string,
    public canjes: number
  ) {}
}

export class NotificacionAmigo {
  constructor(public login: string, public avatar: string, public country: string, public fecha?: dayjs.Dayjs) {}
}
