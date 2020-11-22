export interface IPrivilege {
  id?: number;
}

export class Privilege implements IPrivilege {
  constructor(public id?: number) {}
}
