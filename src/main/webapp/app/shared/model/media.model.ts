import { MediaType } from 'app/shared/model/enumerations/media-type.model';

export interface IMedia {
  id?: number;
  type?: MediaType;
  location?: string;
  fileName?: string;
  extention?: string;
  isDefault?: boolean;
  description?: string;
}

export class Media implements IMedia {
  constructor(
    public id?: number,
    public type?: MediaType,
    public location?: string,
    public fileName?: string,
    public extention?: string,
    public isDefault?: boolean,
    public description?: string
  ) {
    this.isDefault = this.isDefault || false;
  }
}
