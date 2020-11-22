import { IAttribute } from 'app/shared/model/attribute.model';
import { IProperty } from 'app/shared/model/property.model';

export interface ICategory {
  id?: number;
  categorName?: string;
  description?: string;
  attributes?: IAttribute[];
  properties?: IProperty[];
  parents?: ICategory[];
  children?: ICategory;
}

export class Category implements ICategory {
  constructor(
    public id?: number,
    public categorName?: string,
    public description?: string,
    public attributes?: IAttribute[],
    public properties?: IProperty[],
    public parents?: ICategory[],
    public children?: ICategory
  ) {}
}
