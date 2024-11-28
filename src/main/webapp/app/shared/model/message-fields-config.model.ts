import { IMessageIsoConfig } from 'app/shared/model/message-iso-config.model';
import { IMessageFieldType } from 'app/shared/model/message-field-type.model';

export interface IMessageFieldsConfig {
  id?: number;
  name?: string;
  fieldLength?: number;
  messageIsoConfig?: IMessageIsoConfig | null;
  messageFieldType?: IMessageFieldType | null;
}

export const defaultValue: Readonly<IMessageFieldsConfig> = {};
