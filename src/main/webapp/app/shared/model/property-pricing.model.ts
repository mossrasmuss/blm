export interface IPropertyPricing {
  id?: number;
  defaultPrice?: number;
  currentPrice?: number;
  description?: string;
}

export class PropertyPricing implements IPropertyPricing {
  constructor(public id?: number, public defaultPrice?: number, public currentPrice?: number, public description?: string) {}
}
