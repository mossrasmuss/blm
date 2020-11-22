import { Moment } from 'moment';
import { ISalesProperty } from 'app/shared/model/sales-property.model';
import { ICustomer } from 'app/shared/model/customer.model';

export interface IUserAccount {
  id?: number;
  usrname?: string;
  pWd?: string;
  dateCreated?: Moment;
  salesProperties?: ISalesProperty[];
  customer?: ICustomer;
}

export class UserAccount implements IUserAccount {
  constructor(
    public id?: number,
    public usrname?: string,
    public pWd?: string,
    public dateCreated?: Moment,
    public salesProperties?: ISalesProperty[],
    public customer?: ICustomer
  ) {}
}
