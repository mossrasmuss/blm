export interface IPropertyGroup {
  id?: number;
  groupName?: string;
  description?: string;
}

export class PropertyGroup implements IPropertyGroup {
  constructor(public id?: number, public groupName?: string, public description?: string) {}
}
