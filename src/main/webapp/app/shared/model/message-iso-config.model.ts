import { IAcquirer } from 'app/shared/model/acquirer.model';
import { IMessageTypeIndicator } from 'app/shared/model/message-type-indicator.model';

export interface IMessageIsoConfig {
  id?: number;
  name?: string;
  filename?: string;
  acquirer?: IAcquirer | null;
  messageTypeIndicator?: IMessageTypeIndicator | null;
}

export const defaultValue: Readonly<IMessageIsoConfig> = {};
