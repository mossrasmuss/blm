import { ISalesProperty } from 'app/shared/model/sales-property.model';
import { IAttributeValue } from 'app/shared/model/attribute-value.model';
import { ICategory } from 'app/shared/model/category.model';

export interface IProperty {
  id?: number;
  area?: number;
  description?: string;
  salesProperty?: ISalesProperty;
  attributeValues?: IAttributeValue[];
  category?: ICategory;
}

export class Property implements IProperty {
  constructor(
    public id?: number,
    public area?: number,
    public description?: string,
    public salesProperty?: ISalesProperty,
    public attributeValues?: IAttributeValue[],
    public category?: ICategory
  ) {}
}
