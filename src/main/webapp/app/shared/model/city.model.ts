export interface ICity {
  id?: number;
  cityyName?: string;
}

export class City implements ICity {
  constructor(public id?: number, public cityyName?: string) {}
}
