export interface IAddress {
  id?: number;
  addressName?: string;
}

export class Address implements IAddress {
  constructor(public id?: number, public addressName?: string) {}
}
