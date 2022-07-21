import dayjs from 'dayjs/esm';

export class Registration {
  constructor(
    public login: string,
    public email: string,
    public password: string,
    public langKey: string,
    public fechaNacimiento: dayjs.Dayjs,
    public pais: string,
    public activationEndpoint: string
  ) {}
}
