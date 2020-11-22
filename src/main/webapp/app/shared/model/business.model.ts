import { Moment } from 'moment';

export interface IBusiness {
  id?: number;
  businessName?: string;
  year?: Moment;
}

export class Business implements IBusiness {
  constructor(public id?: number, public businessName?: string, public year?: Moment) {}
}
