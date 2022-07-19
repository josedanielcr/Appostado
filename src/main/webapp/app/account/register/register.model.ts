export class Registration {
  constructor(
    public login: string,
    public email: string,
    public password: string,
    public langKey: string,
    public fechaNacimiento: string,
    public pais: string,
    public activationEndpoint: string
  ) {}
}
