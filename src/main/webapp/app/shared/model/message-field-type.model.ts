import { IFieldType } from 'app/shared/model/field-type.model';

export interface IMessageFieldType {
  id?: number;
  name?: string;
  fieldType?: IFieldType | null;
}

export const defaultValue: Readonly<IMessageFieldType> = {};
