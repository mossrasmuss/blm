import { Moment } from 'moment';
import { IUserAccount } from 'app/shared/model/user-account.model';

export interface ICustomer {
  id?: number;
  customerName?: string;
  dob?: Moment;
  accounts?: IUserAccount[];
}

export class Customer implements ICustomer {
  constructor(public id?: number, public customerName?: string, public dob?: Moment, public accounts?: IUserAccount[]) {}
}
