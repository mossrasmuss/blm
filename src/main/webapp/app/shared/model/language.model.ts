export interface ILanguage {
  id?: number;
  languageName?: string;
  code?: string;
}

export class Language implements ILanguage {
  constructor(public id?: number, public languageName?: string, public code?: string) {}
}
