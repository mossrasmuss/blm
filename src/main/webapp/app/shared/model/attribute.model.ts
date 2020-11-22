import { IAttributeValue } from 'app/shared/model/attribute-value.model';
import { ICategory } from 'app/shared/model/category.model';

export interface IAttribute {
  id?: number;
  attributeName?: string;
  description?: string;
  attributeValues?: IAttributeValue[];
  category?: ICategory;
}

export class Attribute implements IAttribute {
  constructor(
    public id?: number,
    public attributeName?: string,
    public description?: string,
    public attributeValues?: IAttributeValue[],
    public category?: ICategory
  ) {}
}
