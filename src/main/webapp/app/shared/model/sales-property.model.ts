import { Moment } from 'moment';
import { IProperty } from 'app/shared/model/property.model';
import { IUserAccount } from 'app/shared/model/user-account.model';
import { SalesType } from 'app/shared/model/enumerations/sales-type.model';
import { SalesStatus } from 'app/shared/model/enumerations/sales-status.model';

export interface ISalesProperty {
  id?: number;
  datePosted?: Moment;
  expiryDate?: Moment;
  type?: SalesType;
  status?: SalesStatus;
  defaultPrice?: number;
  property?: IProperty;
  account?: IUserAccount;
}

export class SalesProperty implements ISalesProperty {
  constructor(
    public id?: number,
    public datePosted?: Moment,
    public expiryDate?: Moment,
    public type?: SalesType,
    public status?: SalesStatus,
    public defaultPrice?: number,
    public property?: IProperty,
    public account?: IUserAccount
  ) {}
}
