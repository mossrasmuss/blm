import { IAttribute } from 'app/shared/model/attribute.model';
import { IProperty } from 'app/shared/model/property.model';

export interface IAttributeValue {
  id?: number;
  value?: string;
  description?: string;
  attribute?: IAttribute;
  property?: IProperty;
}

export class AttributeValue implements IAttributeValue {
  constructor(
    public id?: number,
    public value?: string,
    public description?: string,
    public attribute?: IAttribute,
    public property?: IProperty
  ) {}
}
