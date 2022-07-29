export interface IUser {
  id?: number;
  login?: string;
  imageUrl?: string;
  email?: string;
  firstName?: string;
  lastName?: string;
  activated?: boolean;
}

export class User implements IUser {
  constructor(public id: number, public login: string, public imageUrl: string) {}
}

export function getUserIdentifier(user: IUser): number | undefined {
  return user.id;
}
