export interface IUser {
  id?: number;
  login?: string;
  imageUrl?: string;
  email?: string;
  firstName?: string;
  lastName?: string;
  activated?: boolean;
  authorities?: string[];
}

export class User implements IUser {
  constructor(public id: number, public login: string, public imageUrl: string, public authorities?: string[]) {}
}

export function getUserIdentifier(user: IUser): number | undefined {
  return user.id;
}
